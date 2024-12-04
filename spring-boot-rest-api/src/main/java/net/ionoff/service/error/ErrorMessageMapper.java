package net.ionoff.service.error;

import lombok.AllArgsConstructor;
import net.ionoff.service.api.dto.ErrorMessageDto;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.springframework.context.MessageSource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import javax.validation.ConstraintViolation;
import java.beans.Introspector;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;

@Component
@AllArgsConstructor
public class ErrorMessageMapper {

    private MessageSource messageSource;

    public static final Locale NO_LOCALE = Locale.forLanguageTag("");

    public ErrorMessageDto fromObjectError(@NonNull ObjectError objectError) {
        ConstraintViolationImpl<?> source = objectError.unwrap(ConstraintViolationImpl.class);
        String errorCode = getErrorCode(source, objectError.getCode(), messageSource);
        String errorMessage = objectError.getDefaultMessage();
        String fieldError = "";
        if (objectError instanceof FieldError) {
            fieldError = ((FieldError)objectError).getField();
        }

        Collection<String> expressionFieldErrors = (Collection) source.getExpressionVariables().get("fields");
        if (expressionFieldErrors != null && !expressionFieldErrors.isEmpty()) {
            String fieldNameErrorPrefix = fieldError.isEmpty() ? "" : fieldError;
            List<String> errorFields = expressionFieldErrors.stream().map(f ->
                getFullFieldErrorName(fieldNameErrorPrefix, f)
            ).collect(Collectors.toList());
            return ErrorMessageDto.builder()
                    .code(errorCode)
                    .message(errorMessage)
                    .fields(errorFields)
                    .build();
        } else {
            return fieldError.isEmpty()
                    ? ErrorMessageDto.builder()
                        .code(errorCode)
                        .message(errorMessage)
                        .fields(Collections.emptyList()).build()
                    : ErrorMessageDto.builder()
                        .code(errorCode)
                        .message(errorMessage)
                        .fields(singletonList(fieldError)).build();
        }
    }

    public ErrorMessageDto fromConstraintViolation(ConstraintViolation<?> constraintViolation) {
        String defaultErrorCode = constraintViolation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName();
        String errorCode = getErrorCode(constraintViolation, defaultErrorCode, messageSource);
        String errorMessage = constraintViolation.getMessage();
        String[] properties = constraintViolation.getPropertyPath().toString().split("\\.");
        String fieldError = String.join(".", Arrays.copyOfRange(properties, 1, properties.length));

        if (isValidatedRequestBody(constraintViolation, fieldError)) {
            fieldError = fieldError.replace("body", "");
        }
        Collection<String> expressionFieldErrors = constraintViolation instanceof ConstraintViolationImpl
                ?  (Collection) ((ConstraintViolationImpl) constraintViolation).getExpressionVariables().get("fields")
                : Collections.emptyList();
        if (expressionFieldErrors != null && !expressionFieldErrors.isEmpty()) {
            String fieldNameErrorPrefix = fieldError.isEmpty() ? "" : fieldError;
            List<String> errorFields = expressionFieldErrors.stream().map(f ->
                    getFullFieldErrorName(fieldNameErrorPrefix, f)
            ).collect(Collectors.toList());
            return ErrorMessageDto.builder()
                    .code(errorCode)
                    .message(errorMessage)
                    .fields(errorFields)
                    .build();
        } else {
            return fieldError.isEmpty()
                    ? ErrorMessageDto.builder()
                    .code(errorCode)
                    .message(errorMessage)
                    .fields(Collections.emptyList()).build()
                    : ErrorMessageDto.builder()
                    .code(errorCode)
                    .message(errorMessage)
                    .fields(singletonList(fieldError)).build();
        }
    }

    private boolean isValidatedRequestBody(ConstraintViolation<?> constraintViolation, String fieldError) {
        // it's validating request body of controller method
        // as it throws ConstraintViolationException when the request body is instant of list
        return constraintViolation.getRootBeanClass().getSimpleName().endsWith("Controller")
                && fieldError.startsWith("body");
    }

    private String getFullFieldErrorName(String object, String errorField) {
        if (errorField.startsWith("[")) {
            return object + errorField;
        } else {
            return object.isEmpty() ? errorField : object + "." + errorField;
        }
    }

    private String getErrorCode(ConstraintViolation<?> source, String defaultErrorCode, MessageSource messageSource) {
        String errorCode = decapitalize(defaultErrorCode);
        String errorCodeKey = source.getConstraintDescriptor().getAnnotation().annotationType().getCanonicalName() + ".code";
        return messageSource.getMessage(errorCodeKey, null, errorCode, NO_LOCALE);
    }

    public String getErrorCode(Exception e) {
        return decapitalize(e == null ? "Exception" : e.getClass().getSimpleName());
    }

    public String decapitalize(String className) {
        return Introspector.decapitalize(className == null ? "null" : className);
    }
}
