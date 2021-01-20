package net.ionoff.common.validation.validator;

import net.ionoff.common.validation.constraints.NotAcceptedValue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.Silent.class)
public class NotAcceptedValueNumberTest extends AbstractConstraintValidatorTest{

    @Test
    public void isValid_shouldReturnTrue_whenValueIsNull() throws NoSuchFieldException {
        // GIVEN instance
        NotAcceptedValueNumberValidator validator = new NotAcceptedValueNumberValidator(dynamicConfiguration);
        validator.initialize(givenAnnotationWithValue());

        // WHEN
        boolean result = validator.isValid(null, validatorContext);

        // THEN
        assertThat(result, equalTo(true));
    }

    @Test
    public void isValid_shouldReturnTrue_whenValueIsNotInNotAcceptedValueList() throws NoSuchFieldException {
        // GIVEN instance
        NotAcceptedValueNumberValidator validator = new NotAcceptedValueNumberValidator(dynamicConfiguration);
        validator.initialize(givenAnnotationWithValue());

        BigDecimal object = BigDecimal.valueOf(5);

        // WHEN
        boolean result = validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(true));
    }

    @Test
    public void isValid_shouldReturnTrue_whenValueIsNotInAcceptedValueListExpression() throws NoSuchFieldException {
        // GIVEN instance
        Long object = 10L;
        NotAcceptedValueNumberValidator validator = new NotAcceptedValueNumberValidator(dynamicConfiguration);
        validator.initialize(givenAnnotationWithExpression());

        // WHEN
        boolean result = validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(true));
    }

    @Test
    public void isValid_shouldReturnFalse_whenValueIsInNotAcceptedValueList() throws NoSuchFieldException {
        // GIVEN instance
        NotAcceptedValueNumberValidator validator = new NotAcceptedValueNumberValidator(dynamicConfiguration);
        validator.initialize(givenAnnotationWithValue());

        Integer object = 1;

        // WHEN
        boolean result = validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(false));
        verifyAddMesssageParameter("value", object);
    }

    @Test
    public void isValid_shouldReturnFalse_whenValueIsInNotAcceptedValueListExpression() throws NoSuchFieldException {
        // GIVEN instance
        BigDecimal object = BigDecimal.ONE;
        NotAcceptedValueNumberValidator validator = new NotAcceptedValueNumberValidator(dynamicConfiguration);
        validator.initialize(givenAnnotationWithExpression());

        // WHEN
        boolean result = validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(false));
        verifyAddMesssageParameter("value", object);
    }

    private static NotAcceptedValue givenAnnotationWithValue() throws NoSuchFieldException {
        final class ApiModel {
            @NotAcceptedValue(value = "1,2,3")
            private String s;
        }
        return ApiModel.class.getDeclaredField("s").getAnnotation(NotAcceptedValue.class);
    }

    private static NotAcceptedValue givenAnnotationWithExpression() throws NoSuchFieldException {
        final class ApiModel {
            @NotAcceptedValue(expression = "$reflection::net.ionoff.common.validation.constant.Reflections#notAcceptedNumbers")
            private String s;
        }
        return ApiModel.class.getDeclaredField("s").getAnnotation(NotAcceptedValue.class);
    }

}
