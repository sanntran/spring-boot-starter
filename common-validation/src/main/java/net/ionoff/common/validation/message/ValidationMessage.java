package net.ionoff.common.validation.message;

import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.springframework.context.MessageSource;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class ValidationMessage extends ServiceMessage {

    static final Locale NO_LOCALE = Locale.forLanguageTag("");

    private List<String> fields;

    public ValidationMessage(String code, String message, String... fields) {
        super(code, message);
        this.fields = Arrays.asList(fields);
    }

    public ValidationMessage(String code, String message, Collection<String> fields) {
        super(code, message);
        this.fields = (fields == null ? Collections.emptyList() : new ArrayList<>(fields));
    }

    public List<String> getFields() {
        return fields;
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (!(object instanceof ValidationMessage)) {
            return false;
        }

        ValidationMessage other = (ValidationMessage) object;
        return Objects.equals(this.code, other.code)
                && Objects.equals(this.fields, other.fields)
                && Objects.equals(this.message, other.message);
    }

    @Override
    public String toString() {
        return new StringBuilder().append("code=").append(code)
                .append(", message=").append(message)
                .append(", fields=").append(String.join(",", fields)).toString();
    }

    public static ValidationMessage fromObjectError(ObjectError objectError, MessageSource messageSource) {

        ConstraintViolationImpl<?> source = objectError.unwrap(ConstraintViolationImpl.class);
        String errorCode = getErrorCode(source, objectError.getCode(), messageSource);
        String errorMessage = objectError.getDefaultMessage();
        String fieldError = "";
        if (objectError instanceof FieldError) {
            fieldError = ((FieldError) objectError).getField();
        }
        // variable "fields" is added when validate (see AbstractValidator.addFieldsVariable
        Collection<String> expressionFieldErrors = (Collection)(source.getExpressionVariables().get("fields"));
        if (expressionFieldErrors == null || expressionFieldErrors.isEmpty()) {
            return new ValidationMessage(errorCode, errorMessage, fieldError);
        } else {
            String fieldNameErrorPrefix = fieldError.isEmpty() ? "" : fieldError;
            List<String> errorFields = expressionFieldErrors.stream()
                    .map(f -> getFullFieldErrorName(fieldNameErrorPrefix, f))
                    .collect(Collectors.toList());
            return new ValidationMessage(errorCode, errorMessage, errorFields);
        }
    }

    private static String getFullFieldErrorName(String object, String errorField) {
        if (errorField.startsWith("[")) {
            // when we validate an array, errorField is field error of object in array Ex: [1].name
            return object + errorField;
        }
        // when we validate an object (not array), errorField is field error of object Ex: name
        return object.isEmpty() ? errorField : object + "." + errorField;
    }

    private static String getErrorCode(ConstraintViolationImpl<?> source, String defaultErrorCode, MessageSource messageSource) {
        // doesn't log error // may have no message for
        char[] defaultErrorCodeChars = defaultErrorCode.toCharArray();
        defaultErrorCodeChars[0] = Character.toLowerCase(defaultErrorCodeChars[0]);
        String formattedDefaultErrorCode = new String(defaultErrorCodeChars);
        // error code is in message properties. ex net.ionoff.common.validation.constraints.MinItem.code=minItem
        String errorCodeKey = source.getConstraintDescriptor().getAnnotation().annotationType().getCanonicalName() + ".code";
        return messageSource.getMessage(errorCodeKey, null, formattedDefaultErrorCode, NO_LOCALE);
    }

    public static ValidationMessage notNull(String field) {
        return new ValidationMessage("notNull",
                String.format("Value of field '%s' must be not-null", field),
                field);
    }

    public static ValidationMessage notNull(String fieldName, String field, String condition) {
        return new ValidationMessage("notNull",
                String.format("Value of all fields '%s' must not be null when field '%s' %s", fieldName, field, condition),
                fieldName, field);
    }

    public static ValidationMessage notEmpty(String field) {
        return new ValidationMessage("notEmpty",
                String.format("Value of field '%s' must be not empty", field),
                field);
    }

    public static ValidationMessage notAllNull(String... fields) {
        return new ValidationMessage("notAllNull",
                String.format("Value of at least one field '%s' must be not-null", String.join(",", fields)),
                fields);

    }
    public static ValidationMessage allNullOrAllNotNull(String... fields) {
        return new ValidationMessage("allNullOrAllNotNull",
                String.format("Value of all fields '%s' must be all null or not-null", String.join(",", fields)),
                fields);
    }

    public static ValidationMessage notExist(String field, String value) {
        return new ValidationMessage("notExist",
                String.format("Value of field '%s' does not exist: '%s'", field, value),
                field);
    }

    public static ValidationMessage notExist(String field, List<String> values) {
        return new ValidationMessage("notExist",
                String.format("Values of field '%s' do not exist: '%s'", field, String.join(",", values)),
                field);
    }


    public static ValidationMessage notValid(String field, String value) {
        return new ValidationMessage("notValid",
                String.format("Value of field '%s' is invalid: '%s'", field, value),
                field);
    }


    public static ValidationMessage notValid(String field, List<String> values) {
        return new ValidationMessage("notValid",
                String.format("Values of field '%s' are invalid: '%s'", field, String.join(",", values)),
                field);
    }

    public static ValidationMessage notBlank(String field) {
        return new ValidationMessage("notBlank",
                String.format("Value of field '%s' must be not-blank", field),
                field);
    }

    public static ValidationMessage notEmptyArray(String field) {
        return new ValidationMessage("notEmptyArray",
                String.format("Value of field '%s' must be not-empty array", field),
                field);
    }

    public static ValidationMessage notAbleToUpdate(String field) {
        return new ValidationMessage("notAbleToUpdate",
                String.format("Value of field '%s' is not allowed to updated", field),
                field);
    }

    public static ValidationMessage notAccepted(String field, List<String> values) {
        return new ValidationMessage("notAccepted",
                String.format("Value of field '%s' are not accepted: '%s'", field, String.join(",", values)),
                field);

    }

    public static ValidationMessage notAccepted(String field, String value) {
        return new ValidationMessage("notAccepted",
                String.format("Value of field '%s' is not accepted: '%s'", field, value),
                field);

    }

    public static ValidationMessage notDuplicated(String field, String value) {
        return new ValidationMessage("notDuplicated",
                String.format("Value of field '%s' must not be conflicted with existing value: '%s'", field, value),
                field);
    }

    public static ValidationMessage notDuplicated(List<String> fields, String value) {
        return new ValidationMessage("notDuplicated",
                String.format("Value of fields '%s' is duplicated with each other: '%s'",
                        String.join(", ", fields), value), fields);
    }


    public static ValidationMessage maxLength(String field, int maxLength) {
        return new ValidationMessage("maxLength",
                String.format("Value of field '%s' must be not over maximum length '%s'", field, maxLength),
                field);
    }

    public static ValidationMessage invalidPhoneNumber(String field, String value) {
        return new ValidationMessage("invalidPhoneNumber",
                String.format("Value '%s' of field '%s' must be valid phone number", value, field),
                field);
    }

    public static ValidationMessage invalidUUID(String field, String value) {
        return new ValidationMessage("invalidUUID",
                String.format("Value of field '%s' is not valid UUID: '%s'", value, field),
                field);
    }

    public static ValidationMessage invalidUUIDs(String field, List<String> values) {
        return new ValidationMessage("invalidUUIDs",
                String.format("Value of field '%s' are not valid UUID: '%s'", field, String.join(", ", values)),
                field);
    }

    public static ValidationMessage invalidEmailAddress(String field, String value) {
        return new ValidationMessage("invalidEmailAddress",
                String.format("Value '%s' of field '%s' is not valid email address: 's'", value, field),
                field);
    }

    public static ValidationMessage invalidUnsignedInteger(String field, String value) {
        return new ValidationMessage("invalidUnsignedInteger",
                String.format("Value of field '%s' is not valid unsigned integer: '%s'", field, value),
                field);
    }

    public static ValidationMessage invalidInteger(String field, String value) {
        return new ValidationMessage("invalidInteger",
                String.format("Value of field '%s' is not valid integer: '%s'", field, value),
                field);
    }

    public static ValidationMessage invalidNumber(String field, String value) {
        return new ValidationMessage("invalidInteger",
                String.format("Value of field '%s' is not valid number: '%s'", field, value),
                field);
    }

    public static ValidationMessage invalidTimeZone(String field, String value) {
        return new ValidationMessage("invalidTimeZone",
                String.format("Value of field '%s' is not valid timezone: '%s'", field, value),
                field);
    }

    public static ValidationMessage maxValue(String field, BigDecimal maxValue) {
        return new ValidationMessage("maxValue",
                String.format("Field '%s' is invalid. Value must not be greater than %s", field, maxValue),
                field);
    }

    public static ValidationMessage minValue(String field, BigDecimal minValue) {
        return new ValidationMessage("minValue",
                String.format("Field '%s' is invalid. Minimum value is %s", field, minValue),
                field);
    }

    public static ValidationMessage maxItem(String field, int maxItem) {
        return new ValidationMessage("maxItem",
                String.format("Value of field '%s' must not contains more than %s items", field, maxItem),
                field);
    }

    public static ValidationMessage maxItemType(String field, int maxItem, String itemType) {
        return new ValidationMessage("maxItem",
                String.format("Value of field '%s' must not contains more than '%s '%s' items", field, maxItem, itemType),
                field);
    }

    public static ValidationMessage minItem(String field, int minItem) {
        return new ValidationMessage("minItem",
                String.format("Value of field '%s' must not contains less than '%s' items", field, minItem),
                field);
    }

    public static ValidationMessage minItemType(String field, int minItem, String itemType) {
        return new ValidationMessage("minItem",
                String.format("Value of field '%s' must not contains less than '%s' '%s' items", field, minItem, itemType),
                field);
    }

    public static ValidationMessage afterDate(String fieldName, String date) {
        return new ValidationMessage("afterDate",
                String.format("Field '%s' is invalid. Value must be after date '%s'", fieldName, date),
                fieldName);
    }

    public static ValidationMessage invalidFileSize(String field, BigDecimal maxMb) {
        return new ValidationMessage("invalidFileSize",
                String.format("File must be not-empty and file size must not be greater than %sMB", maxMb),
                field);
    }

    public static ValidationMessage cannotUpdateInactiveResource() {
        return new ValidationMessage("cannotUpdateInactiveResource",
                "The resource is currently inactive. It does not allow to update");
    }

    public static ValidationMessage cannotDeleteResourceInUse(String id) {
        return new ValidationMessage("cannotDeleteResourceInUse",
                String.format("Cannot delete resource '%s'. It is currently in use", id));
    }
}
