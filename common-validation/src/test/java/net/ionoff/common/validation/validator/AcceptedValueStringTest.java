package net.ionoff.common.validation.validator;

import net.ionoff.common.validation.constraints.AcceptedValue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AcceptedValueStringTest extends AbstractConstraintValidatorTest{


    @Test
    public void isValid_shouldReturnTrue_whenObjectIsNull() throws NoSuchFieldException {
        // GIVEN instance
        String object = null;
        AcceptedValueStringValidator validator = new AcceptedValueStringValidator(dynamicConfiguration);
        validator.initialize(givenAnnotationWithValue());

        // WHEN
        boolean result = validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(true));
    }

    @Test
    public void isValid_shouldReturnFalse_whenValueIsNotInAcceptedValueList() throws NoSuchFieldException {
        // GIVEN instance
        AcceptedValueStringValidator validator = new AcceptedValueStringValidator(dynamicConfiguration);
        validator.initialize(givenAnnotationWithValue());

        String object = "K";

        // WHEN
        boolean result = validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(false));
        verifyAddMesssageParameter("value", "K");
    }

    @Test
    public void isValid_shouldReturnTrue_whenValueIsInAcceptedValueList() throws NoSuchFieldException {
        // GIVEN instance
        String object = "A";
        AcceptedValueStringValidator validator = new AcceptedValueStringValidator(dynamicConfiguration);
        validator.initialize(givenAnnotationWithExpression());

        // WHEN
        boolean result = validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(true));
    }

    private static AcceptedValue givenAnnotationWithValue() throws NoSuchFieldException {
        final class ApiModel {
            @AcceptedValue(value = "A,B,C")
            private String s;
        }
        return ApiModel.class.getDeclaredField("s").getAnnotation(AcceptedValue.class);
    }
    
    private static AcceptedValue givenAnnotationWithExpression() throws NoSuchFieldException {
        final class ApiModel {
            @AcceptedValue(expression = "$reflection::net.ionoff.common.validation.constant.Reflections#acceptedStrings")
            private String s;
        }
        return ApiModel.class.getDeclaredField("s").getAnnotation(AcceptedValue.class);
    }

}
