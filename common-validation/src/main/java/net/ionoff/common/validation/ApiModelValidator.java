package net.ionoff.common.validation;

import io.swagger.annotations.ApiModelProperty;
import net.ionoff.common.validation.annotation.Constant;
import net.ionoff.common.validation.annotation.IValidator;
import net.ionoff.common.validation.annotation.parser.IValidatorBuilder;
import net.ionoff.common.validation.annotation.parser.ValidatorParser;
import net.ionoff.common.validation.DynamicConfiguration;

import java.lang.reflect.Method;
import java.util.*;

public final class ApiModelValidator {

    private final Class<?> apiModelClass;
    private final Map<String, ApiModelFieldValidator> fieldValidatorMap;

    ApiModelValidator(Class<?> apiModelClass) {
        this.apiModelClass = apiModelClass;
        this.fieldValidatorMap = new HashMap<>();
    }
    
    public static ApiModelValidator createForClass(
            Class<?> clazz,
            Map<Class<?>, IValidatorBuilder<?>> validatorBuilders
    ) {
        ApiModelValidator modelValidator = new ApiModelValidator(clazz);
        for (Method method : clazz.getMethods()) {
            Optional<ApiModelFieldValidator> apiModelFieldValidator = getApiModelFieldValidator(method, validatorBuilders);
            apiModelFieldValidator.ifPresent((validator -> modelValidator.getFieldValidatorMap().put(validator.getFieldName(), validator)));
        }
        return modelValidator;
    }

    public List<IValidator> getFieldValidators(String fieldName) {
        ApiModelFieldValidator fieldValidator = fieldValidatorMap.get(fieldName);
        return fieldValidator == null ? Collections.emptyList() : fieldValidator.getFieldValidators();
    }

    public void setDynamicConfiguration(DynamicConfiguration dynamicConfiguration) {
        for (ApiModelFieldValidator fieldValidator : fieldValidatorMap.values()) {
            fieldValidator.setDynamicConfiguration(dynamicConfiguration);
        }
    }

    Class<?> getApiModelClass() {
        return apiModelClass;
    }

    Map<String, ApiModelFieldValidator> getFieldValidatorMap() {
        return fieldValidatorMap;
    }

    private static Optional<ApiModelFieldValidator> getApiModelFieldValidator(
            Method method,
            Map<Class<?>, IValidatorBuilder<?>> validatorBuilders
    ) {
        String field = null;
        if (method.getName().startsWith("is")) {
            field = getField(method, "is");
        } else if (method.getName().startsWith("get")) {
            field = getField(method, "get");
        }
        if (field == null) {
            return Optional.empty();
        }
        ApiModelProperty annotation = method.getAnnotation(ApiModelProperty.class);
        if (annotation == null || annotation.value() == null) {
            return Optional.empty();
        }
        return Optional.of(newApiModelFieldValidator(field, annotation, validatorBuilders));
    }

    private static String getField(Method method, String prefix) {
        String field = method.getName().substring(prefix.length());
        field = new StringBuilder()
                .append(String.valueOf(field.charAt(0)).toLowerCase())
                .append(field.substring(1)).toString();
        return field;
    }

    private static ApiModelFieldValidator newApiModelFieldValidator(
            String fieldName,
            ApiModelProperty annotation,
            Map<Class<?>, IValidatorBuilder<?>> validatorBuilders
    ) {
        ApiModelFieldValidator apiModelFieldValidator = new ApiModelFieldValidator(fieldName);
        apiModelFieldValidator.getFieldValidators().addAll(createAnnotatedValidatorFromApiModelProperty(
                annotation, validatorBuilders
        ));
        return apiModelFieldValidator;
    }

    private static List<IValidator> createAnnotatedValidatorFromApiModelProperty(
            ApiModelProperty annotation,
            Map<Class<?>, IValidatorBuilder<?>> validatorBuilders
    ) {
        String propertyDescription = annotation.value();
        List<IValidator> annotatedValidators = new ArrayList<>();

        String[] validations = propertyDescription.split("\\|");
        for (String validationDefinition : validations) {
            validationDefinition = validationDefinition.trim();
            if (!validationDefinition.startsWith(Constant.VALIDATOR_PREFIX)) {
                continue;
            }
            annotatedValidators.add(ValidatorParser.parseValidator(validationDefinition, validatorBuilders));
        }
        return annotatedValidators;
    }
}
