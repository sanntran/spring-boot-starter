package net.ionoff.common.validation.message;

import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import javax.validation.constraints.NotNull;
import javax.validation.metadata.ConstraintDescriptor;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ValidationMessageTest {

    @Test
    public void fromObjectError_returnCorrectValidationMessage_whenObjectErrorIsFieldErrorAndNoExpressionFieldError() {
        Map<String, List<String>> expressionVariables = new HashMap<>();
        ObjectError objectError = mockFieldError("testField", NotNull.class, expressionVariables);

        MessageSource messageSource = mockMessageSource("javax.validation.constraints.NotNull.code", "notNull");

        ValidationMessage validationMessage = ValidationMessage.fromObjectError(objectError, messageSource);

        assertThat(validationMessage.getCode(), equalTo("notNull"));
        assertThat(validationMessage.getFields(), equalTo(Arrays.asList("testField")));
        assertThat(validationMessage.getMessage(), equalTo("Test error message"));

    }

    @Test
    public void fromObjectError_returnCorrectValidationMessage_whenObjectErrorIsFieldErrorAndManyExpressionFieldErrors() {
        Map<String, List<String>> expressionVariables = new HashMap<>();
        expressionVariables.put("fields", Arrays.asList("testField1", "testField2"));
        ObjectError objectError = mockFieldError("testField", NotNull.class, expressionVariables);

        MessageSource messageSource = mockMessageSource("javax.validation.constraints.NotNull.code", "notNull");

        ValidationMessage validationMessage = ValidationMessage.fromObjectError(objectError, messageSource);

        assertThat(validationMessage.getCode(), equalTo("notNull"));
        assertThat(validationMessage.getFields(), equalTo(Arrays.asList("testField.testField1", "testField.testField2")));
        assertThat(validationMessage.getMessage(), equalTo("Test error message"));

    }

    @Test
    public void fromObjectError_returnCorrectValidationMessage_whenObjectErrorIsFieldErrorAndManyArrayItemExpressionFieldErrors() {
        Map<String, List<String>> expressionVariables = new HashMap<>();
        expressionVariables.put("fields", Arrays.asList("[0].testField1", "[1].testField2"));
        ObjectError objectError = mockFieldError("testField", NotNull.class, expressionVariables);

        MessageSource messageSource = mockMessageSource("javax.validation.constraints.NotNull.code", "notNull");

        ValidationMessage validationMessage = ValidationMessage.fromObjectError(objectError, messageSource);

        assertThat(validationMessage.getCode(), equalTo("notNull"));
        assertThat(validationMessage.getFields(), equalTo(Arrays.asList("testField[0].testField1", "testField[1].testField2")));
        assertThat(validationMessage.getMessage(), equalTo("Test error message"));

    }

    @Test
    public void fromObjectError_returnCorrectValidationMessage_whenObjectErrorHasManyFieldErrors() {
        Map<String, List<String>> expressionVariables = new HashMap<>();
        expressionVariables.put("fields", Arrays.asList("testField1", "testField2"));
        ObjectError objectError = mockObjectError(NotNull.class, expressionVariables);

        MessageSource messageSource = mockMessageSource("javax.validation.constraints.NotNull.code", "notNull");

        ValidationMessage validationMessage = ValidationMessage.fromObjectError(objectError, messageSource);

        assertThat(validationMessage.getCode(), equalTo("notNull"));
        assertThat(validationMessage.getFields(), equalTo(Arrays.asList("testField1", "testField2")));
        assertThat(validationMessage.getMessage(), equalTo("Test error message"));

    }


    private MessageSource mockMessageSource(String codeKey, String defaultCodeValue) {
        MessageSource messageSource = mock(MessageSource.class);
        when(messageSource.getMessage(
                codeKey, null, defaultCodeValue, ValidationMessage.NO_LOCALE))
                .thenReturn(defaultCodeValue);
        return messageSource;
    }

    private ConstraintViolationImpl mockConstraintViolation(Class annotationType, Map<String, List<String>> expressionVariables) {
        ConstraintViolationImpl constraintViolation = mock(ConstraintViolationImpl.class);
        when(constraintViolation.getExpressionVariables()).thenReturn(expressionVariables);
        ConstraintDescriptor constraintDescriptor = mock(ConstraintDescriptor.class);
        when(constraintViolation.getConstraintDescriptor()).thenReturn(constraintDescriptor);
        Annotation constraintAnnotation = mock(Annotation.class);
        when(constraintDescriptor.getAnnotation()).thenReturn(constraintAnnotation);
        when(constraintAnnotation.annotationType()).thenReturn(annotationType);
        return constraintViolation;
    }

    private ObjectError mockFieldError(String field, Class annotationType, Map<String, List<String>> expressionVariables) {
        FieldError objectError = mock(FieldError.class);
        when(objectError.getField()).thenReturn(field);
        setupObjectError(objectError, annotationType, expressionVariables);
        return objectError;
    }

    private ObjectError mockObjectError(Class annotationType, Map<String, List<String>> expressionVariables) {
        ObjectError objectError = mock(ObjectError.class);
        setupObjectError(objectError, annotationType, expressionVariables);
        return objectError;
    }

    private void setupObjectError(ObjectError objectError, Class annotationType, Map<String, List<String>> expressionVariables) {
        when(objectError.getCode()).thenReturn(annotationType.getSimpleName());
        when(objectError.getDefaultMessage()).thenReturn("Test error message");
        ConstraintViolationImpl constraintViolation = mockConstraintViolation(annotationType, expressionVariables);
        when(objectError.unwrap(ConstraintViolationImpl.class)).thenReturn(constraintViolation);
    }
}
