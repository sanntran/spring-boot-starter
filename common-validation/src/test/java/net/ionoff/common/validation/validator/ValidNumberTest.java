package net.ionoff.common.validation.validator;

import net.ionoff.common.validation.constraints.ValidNumber;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ValidNumberTest extends AbstractConstraintValidatorTest {

    @Test
    public void isValid_shouldReturnTrue_whenValueIsNull() throws NoSuchFieldException {
        // GIVEN instance
        String object = null;
        ValidNumberValidator validator = new ValidNumberValidator();
        validator.initialize(givenAnnotation());

        // WHEN
        boolean result = validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(true));
    }

    @Test
    public void isValid_shouldReturnTrue_whenValueIsValidNumber() throws NoSuchFieldException {
        // GIVEN instance
        String object = "2.6";
        ValidNumberValidator validator = new ValidNumberValidator();
        validator.initialize(givenAnnotation());

        // WHEN
        boolean result = validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(true));
    }

    @Test
    public void isValid_shouldReturnFalse_whenValueIsNotNumber() throws NoSuchFieldException {
        // GIVEN instance
        String object = "a";
        ValidNumberValidator validator = new ValidNumberValidator();
        validator.initialize(givenAnnotation());

        // WHEN
        boolean result = validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(false));
        verifyAddMesssageParameter("value", object);
    }

    private static ValidNumber givenAnnotation() throws NoSuchFieldException {
        final class ApiModel {
            @ValidNumber
            private String s;
        }
        return ApiModel.class.getDeclaredField("s").getAnnotation(ValidNumber.class);
    }
}
