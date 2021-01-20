package net.ionoff.common.validation.validator;

import net.ionoff.common.validation.constraints.ValidInteger;
import net.ionoff.common.validation.DynamicConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ValidIntegerTest extends AbstractConstraintValidatorTest {

    @Mock
    private DynamicConfiguration dynamicConfiguration;

    private Object apiModel = new Object();
    private String fieldName = "field";


    @Test
    public void isValid_shouldReturnTrue_whenValueIsNull() throws NoSuchFieldException {
        // GIVEN instance
        String object = null;
        ValidIntegerStringValidator validator = new ValidIntegerStringValidator();
        validator.initialize(givenAnnotation());

        // WHEN
        boolean result = validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(true));
    }

    @Test
    public void isValid_shouldReturnTrue_whenValueIsValidInteger() throws NoSuchFieldException {
        // GIVEN instance
        String object = "1";
        ValidIntegerStringValidator validator = new ValidIntegerStringValidator();
        validator.initialize(givenAnnotation());

        // WHEN
        boolean result = validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(true));
    }

    @Test
    public void isValid_shouldReturnFalse_whenValueIsNotInteger() throws NoSuchFieldException {
        // GIVEN instance
        String object = "a";
        ValidIntegerStringValidator validator = new ValidIntegerStringValidator();
        validator.initialize(givenAnnotation());

        // WHEN
        boolean result = validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(false));
    }

    private static ValidInteger givenAnnotation() throws NoSuchFieldException {
        final class ApiModel {
            @ValidInteger
            private String s;
        }
        return ApiModel.class.getDeclaredField("s").getAnnotation(ValidInteger.class);
    }

}
