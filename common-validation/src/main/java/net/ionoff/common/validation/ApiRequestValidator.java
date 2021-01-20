package net.ionoff.common.validation;

import net.ionoff.common.validation.annotation.IValidator;
import net.ionoff.common.validation.exception.RequestValidationException;
import net.ionoff.common.validation.message.ValidationMessage;
import net.ionoff.common.validation.annotation.parser.IValidatorBuilder;
import net.ionoff.common.validation.DynamicConfiguration;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Collections.emptyMap;

public class ApiRequestValidator {

    private final String apiModelPackageName;
    private final DynamicConfiguration dynamicConfiguration;
    private final Map<Class<?>, IValidatorBuilder<?>> validatorBuilders;
    private final Map<Class<?>, ApiModelValidator> apiModelValidatorMap;

    public ApiRequestValidator(
            String apiModelPackageName,
            DynamicConfiguration dynamicConfiguration
    ) {
        this(apiModelPackageName, dynamicConfiguration, emptyMap(), new ConcurrentHashMap<>());
    }

    public ApiRequestValidator(
            String apiModelPackageName,
            DynamicConfiguration dynamicConfiguration,
            Map<Class<?>, IValidatorBuilder<?>> validatorBuilders
    ) {
        this(apiModelPackageName, dynamicConfiguration, validatorBuilders, new ConcurrentHashMap<>());
    }

    private ApiRequestValidator(
            String apiModelPackageName,
            DynamicConfiguration dynamicConfiguration,
            Map<Class<?>, IValidatorBuilder<?>> validatorBuilders,
            Map<Class<?>, ApiModelValidator> apiModelValidatorMap
    ) {
        this.apiModelPackageName = apiModelPackageName;
        this.dynamicConfiguration = dynamicConfiguration;
        this.validatorBuilders = validatorBuilders;
        this.apiModelValidatorMap = apiModelValidatorMap;
    }

    public void validateApiModel(Object apiModel) {
        List<ValidationMessage> validationMessages = getValidationMessages(apiModel);
        if (!validationMessages.isEmpty()) {
            throw new RequestValidationException(validationMessages);
        }
    }

    public List<ValidationMessage> getValidationMessages(Object apiModel) {
        if (apiModel instanceof List) {
            return getValidationMessages(null, (List) apiModel);
        }
        return getValidationMessages(null, apiModel);
    }

    public void validateApiModelField(Object apiModel, String field) {
        List<ValidationMessage> validationMessages = new ArrayList<>();
        ApiModelWrapper apiModelWrapper = new ApiModelWrapper(apiModel);
        ApiModelValidator apiModelValidator = getApiModelValidator(apiModel);

        for (String fieldName : apiModelWrapper.getApiModelFields()) {
            if (fieldName.equals(field)) {
                Object fieldValue = apiModelWrapper.getApiModelFieldValue(fieldName);
                List<IValidator> annotatedValidators = apiModelValidator.getFieldValidators(fieldName);
                List<ValidationMessage> fieldValidationMessages = validateApiModelField(apiModel, fieldName, fieldValue, annotatedValidators);
                validationMessages.addAll(fieldValidationMessages);
            }
        }
        if (!validationMessages.isEmpty()) {
            throw new RequestValidationException(validationMessages);
        }
    }

    private ApiModelValidator getApiModelValidator(Object apiModel) {
        return apiModelValidatorMap.computeIfAbsent(apiModel.getClass(), clazz -> {
            ApiModelValidator apiModelValidator = ApiModelValidator.createForClass(apiModel.getClass(), validatorBuilders);
            apiModelValidator.setDynamicConfiguration(dynamicConfiguration);
            return apiModelValidator;
        });
    }

    private List<ValidationMessage> getValidationMessages(String objectName, List<Object> apiModel) {
        if (apiModel.isEmpty()) {
            return Collections.emptyList();
        }
        List<ValidationMessage> validationMessages = new ArrayList<>();
        for (int i = 0; i < apiModel.size(); i++) {
            String objectIdx = (objectName == null ? "" : objectName) + "[" + i + "]";
            List<ValidationMessage> dtoValidationMessages = getValidationMessages(objectIdx, apiModel.get(i));
            validationMessages.addAll(dtoValidationMessages);
        }
        return validationMessages;
    }

    private List<ValidationMessage> getValidationMessages(String objectName, Object apiModel) {
        List<ValidationMessage> validationMessages = new ArrayList<>();
        ApiModelWrapper apiModelWrapper = new ApiModelWrapper(apiModel);
        ApiModelValidator apiModelValidator = getApiModelValidator(apiModel);

        for (String field : apiModelWrapper.getApiModelFields()) {
            String fieldName = objectName == null ? field : objectName + "." + field;
            Object fieldValue = apiModelWrapper.getApiModelFieldValue(field);
            List<IValidator> annotatedValidators = apiModelValidator.getFieldValidators(field);
            List<ValidationMessage> fieldValidationMessages = validateApiModelField(apiModel, fieldName, fieldValue, annotatedValidators);
            validationMessages.addAll(fieldValidationMessages);
        }
        return validationMessages;
    }

    private List<ValidationMessage> validateApiModelField(Object object, String fieldName, Object fieldValue, List<IValidator> annotatedValidators) {
        List<ValidationMessage> validationMessages = new ArrayList<>();
        for (IValidator validator : annotatedValidators) {
            List<ValidationMessage> validationMessage = validator.validate(fieldName, fieldValue, object);
            validationMessages.addAll(validationMessage);
        }
        if (isApiModel(fieldValue)) {
            List<ValidationMessage> dtoValidationMessages = getValidationMessages(fieldName, fieldValue);
            validationMessages.addAll(dtoValidationMessages);
        }
        if (fieldValue instanceof List) {
            List<?> objects = (List) fieldValue;
            List<ValidationMessage> dtoValidationMessages = validateApiModelFieldItems(fieldName, objects);
            validationMessages.addAll(dtoValidationMessages);
        }
        return validationMessages;
    }

    private List<ValidationMessage> validateApiModelFieldItems(String fieldName, List<?> fieldValueList) {
        if (fieldValueList.isEmpty()) {
            return Collections.emptyList();
        }
        List<ValidationMessage> validationMessages = new ArrayList<>();
        for (int i = 0; i < fieldValueList.size(); i++) {
            if (isApiModel(fieldValueList.get(i))) {
                List<ValidationMessage> dtoValidationMessages = getValidationMessages(fieldName + "[" + i + "]", fieldValueList.get(i));
                validationMessages.addAll(dtoValidationMessages);
            }
        }
        return validationMessages;
    }

    private boolean isApiModel(Object object) {
        if (object == null) {
            return false;
        }
        return object.getClass().getCanonicalName().startsWith(apiModelPackageName);
    }

}
