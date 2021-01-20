package net.ionoff.common.validation.validator;

import net.ionoff.common.validation.constraints.AcceptedValue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AcceptedValueNumberTest extends AbstractConstraintValidatorTest{


    @Test
    public void isValid_shouldReturnTrue_whenObjectIsNull() throws NoSuchFieldException {
        // GIVEN instance
        Integer object = null;
        AcceptedValueNumberValidator validator = new AcceptedValueNumberValidator(dynamicConfiguration);
        validator.initialize(givenAnnotationWithValue());

        // WHEN
        boolean result = validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(true));
    }

    @Test
    public void isValid_shouldReturnFalse_whenValueIsNotInAcceptedValueList() throws NoSuchFieldException {
        // GIVEN instance
        AcceptedValueNumberValidator validator = new AcceptedValueNumberValidator(dynamicConfiguration);
        validator.initialize(givenAnnotationWithValue());

        BigDecimal object = BigDecimal.ONE;

        // WHEN
        boolean result = validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(false));
        verifyAddMesssageParameter("value", object);
    }

    @Test
    public void isValid_shouldReturnTrue_whenValueIsInAcceptedValueList() throws NoSuchFieldException {
        // GIVEN instance
        Integer object = -1;
        AcceptedValueNumberValidator validator = new AcceptedValueNumberValidator(dynamicConfiguration);
        validator.initialize(givenAnnotationWithExpression());

        // WHEN
        boolean result = validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(true));
    }

    private static AcceptedValue givenAnnotationWithValue() throws NoSuchFieldException {
        final class ApiModel {
            @AcceptedValue(value = "-2,-1,0")
            private String s;
        }
        return ApiModel.class.getDeclaredField("s").getAnnotation(AcceptedValue.class);
    }
    
    private static AcceptedValue givenAnnotationWithExpression() throws NoSuchFieldException {
        final class ApiModel {
            @AcceptedValue(expression = "$reflection::net.ionoff.common.validation.constant.Reflections#acceptedNumbers")
            private String s;
        }
        return ApiModel.class.getDeclaredField("s").getAnnotation(AcceptedValue.class);
    }

}
