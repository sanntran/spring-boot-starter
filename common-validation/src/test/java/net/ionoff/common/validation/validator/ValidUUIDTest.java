package net.ionoff.common.validation.validator;

import net.ionoff.common.validation.constraints.ValidUUID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ValidUUIDTest extends AbstractConstraintValidatorTest {

    @Test
    public void isValid_shouldReturnTrue_whenValueIsNull() throws NoSuchFieldException {
        // GIVEN instance
        String object = null;
        ValidUUIDValidator validator = new ValidUUIDValidator();
        validator.initialize(givenAnnotation());

        // WHEN
        boolean result = validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(true));
    }

    @Test
    public void isValid_shouldReturnTrue_whenValueIsValidUUID() throws NoSuchFieldException {
        // GIVEN instance
        String object = UUID.randomUUID().toString();
        ValidUUIDValidator validator = new ValidUUIDValidator();
        validator.initialize(givenAnnotation());

        // WHEN
        boolean result = validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(true));
    }
    
    @Test
    public void isValid_shouldReturnFalse_whenValueIsNotUUID() throws NoSuchFieldException {
        // GIVEN instance
        String object = "A";
        ValidUUIDValidator validator = new ValidUUIDValidator();
        validator.initialize(givenAnnotation());

        // WHEN
        boolean result = validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(false));
        verifyAddMesssageParameter("value", "A");
    }
    
    private static ValidUUID givenAnnotation() throws NoSuchFieldException {
        final class ApiModel {
            @ValidUUID
            private String s;
        }
        return ApiModel.class.getDeclaredField("s").getAnnotation(ValidUUID.class);
    }
}
