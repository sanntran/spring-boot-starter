package net.ionoff.common.validation.validator;

import net.ionoff.common.validation.constraints.ValidTimeZone;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ValidTimeZoneTest extends AbstractConstraintValidatorTest {

    @Test
    public void isValid_shouldReturnTrue_whenObjectIsNull() throws NoSuchFieldException {
        // GIVEN instance
        String object = null;
        ValidTimeZoneValidator validator = new ValidTimeZoneValidator();
        validator.initialize(givenAnnotation());

        // WHEN
        boolean result =  validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(true));
    }

    @Test
    public void isValid_shouldReturnFalse_whenObjectIsNotTimeZone() throws NoSuchFieldException {
        // GIVEN instance
        String object = "in-valid-time-zone";
        ValidTimeZoneValidator validator = new ValidTimeZoneValidator();
        validator.initialize(givenAnnotation());

        // WHEN
        boolean result = validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(false));
        verifyAddMesssageParameter("value", object);
    }

    @Test
    public void isValid_shouldReturnTrue_whenObjectIsATimeZone() throws NoSuchFieldException {
        // GIVEN instance
        String object = "Pacific/Auckland";
        ValidTimeZoneValidator validator = new ValidTimeZoneValidator();
        validator.initialize(givenAnnotation());

        // WHEN
        boolean result = validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(true));
    }

    private static ValidTimeZone givenAnnotation() throws NoSuchFieldException {
        final class ApiModel {
            @ValidTimeZone
            private String s;
        }
        return ApiModel.class.getDeclaredField("s").getAnnotation(ValidTimeZone.class);
    }

}
