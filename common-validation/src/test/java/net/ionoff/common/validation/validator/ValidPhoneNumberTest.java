package net.ionoff.common.validation.validator;

import net.ionoff.common.validation.constraints.ValidPhoneNumber;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ValidPhoneNumberTest extends AbstractConstraintValidatorTest {

    @Test
    public void isValid_shouldReturnTrue_whenObjectIsNull() throws NoSuchFieldException {
        // GIVEN instance
        String object = null;
        ValidPhoneNumberValidator validator = new ValidPhoneNumberValidator(dynamicConfiguration);
        validator.initialize(givenAnnotation());

        // WHEN
        boolean result = validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(true));
    }

    @Test
    public void isValid_shouldReturnTrue_whenNotUseRegexAndObjectIsNotBlank() throws NoSuchFieldException {
        // GIVEN instance
        String object = "ABCD1345678";
        ValidPhoneNumberValidator validator = new ValidPhoneNumberValidator(dynamicConfiguration);
        validator.initialize(givenAnnotation());

        // WHEN
        boolean result = validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(true));
    }

    @Test
    public void isValid_shouldReturnTrue_whenUseRegexAndObjectMatchesRegex() throws NoSuchFieldException {
        // GIVEN instance
        String object = "(123) 456-7890";
        ValidPhoneNumberValidator validator = new ValidPhoneNumberValidator(dynamicConfiguration);
        validator.initialize(givenAnnotationWithExpression());

        // WHEN
        boolean result = validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(true));
    }

    @Test
    public void isValid_shouldReturnFalse_whenNotUseRegexAndObjectIsBlank() throws NoSuchFieldException {
        // GIVEN instance
        String object = "";
        ValidPhoneNumberValidator validator = new ValidPhoneNumberValidator(dynamicConfiguration);
        validator.initialize(givenAnnotationWithExpression());

        // WHEN
        boolean result =  validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(false));
        verifyAddMesssageParameter("value", object);
    }

    @Test
    public void isValid_shouldReturnFalse_whenUseRegexAndObjectNotMatchRegex() throws NoSuchFieldException {
        // GIVEN instance
        String object = "(+351)abcdef";
        ValidPhoneNumberValidator validator = new ValidPhoneNumberValidator(dynamicConfiguration);
        validator.initialize(givenAnnotationWithExpression());

        // WHEN
        boolean result =  validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(false));
        verifyAddMesssageParameter("value", object);
    }

    private static ValidPhoneNumber givenAnnotation() throws NoSuchFieldException {
        final class ApiModel {
            @ValidPhoneNumber
            private String s;
        }
        return ApiModel.class.getDeclaredField("s").getAnnotation(ValidPhoneNumber.class);
    }

    private static ValidPhoneNumber givenAnnotationWithExpression() throws NoSuchFieldException {
        final class ApiModel {
            @ValidPhoneNumber(expression = "(\\+0?1\\s)?\\(?\\d{3}\\)?[\\s.-]\\d{3}[\\s.-]\\d{4}")
            private String s;
        }
        return ApiModel.class.getDeclaredField("s").getAnnotation(ValidPhoneNumber.class);
    }

}
