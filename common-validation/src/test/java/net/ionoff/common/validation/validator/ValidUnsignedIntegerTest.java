package net.ionoff.common.validation.validator;

import net.ionoff.common.validation.constraints.ValidUnsignedInteger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ValidUnsignedIntegerTest extends AbstractConstraintValidatorTest {

    @Test
    public void isValid_shouldReturnTrue_whenValueIsNull() throws NoSuchFieldException {
        // GIVEN instance
        String object = null;
        ValidUnsignedStringValidator validator = new ValidUnsignedStringValidator();
        validator.initialize(givenAnnotation());

        // WHEN
        boolean result = validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(true));
    }

    @Test
    public void isValid_shouldReturnTrue_whenValueIsValidUnsignedInteger() throws NoSuchFieldException {
        // GIVEN instance
        String object = "1";
        ValidUnsignedStringValidator validator = new ValidUnsignedStringValidator();
        validator.initialize(givenAnnotation());

        // WHEN
        boolean result = validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(true));
    }

    @Test
    public void isValid_shouldReturnTrue_whenValueIsValidStringUnsignedInteger() throws NoSuchFieldException {
        // GIVEN instance
        String object = "1";
        ValidUnsignedStringValidator validator = new ValidUnsignedStringValidator();
        validator.initialize(givenAnnotation());

        // WHEN
        boolean result = validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(true));
    }

    @Test
    public void isValid_shouldReturnFalse_whenValueIsStringNotUnsignedInteger() throws NoSuchFieldException {
        // GIVEN instance
        String object = "A";
        ValidUnsignedStringValidator validator = new ValidUnsignedStringValidator();
        validator.initialize(givenAnnotation());

        // WHEN
        boolean result = validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(false));
        verifyAddMesssageParameter("value", object);
    }

    @Test
    public void isValid_shouldReturnFalse_whenValueIsNotUnsignedInteger() throws NoSuchFieldException {
        // GIVEN instance
        String object = "-1";
        ValidUnsignedStringValidator validator = new ValidUnsignedStringValidator();
        validator.initialize(givenAnnotation());

        // WHEN
        boolean result = validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(false));
        verifyAddMesssageParameter("value", "-1");
    }
    
    private static ValidUnsignedInteger givenAnnotation() throws NoSuchFieldException {
        final class ApiModel {
            @ValidUnsignedInteger
            private String s;
        }
        return ApiModel.class.getDeclaredField("s").getAnnotation(ValidUnsignedInteger.class);
    }
}
