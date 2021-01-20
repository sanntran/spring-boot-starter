package net.ionoff.common.validation.validator;

import net.ionoff.common.validation.constraints.ValidEmailAddress;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ValidEmailAddressTest extends AbstractConstraintValidatorTest {

    @Test
    public void isValid_shouldReturnTrue_whenObjectIsNull() throws NoSuchFieldException {
        // GIVEN instance
        String object = null;
        ValidEmailAddressValidator validator = new ValidEmailAddressValidator(dynamicConfiguration);
        validator.initialize(givenAnnotation());

        // WHEN
        boolean result = validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(true));
    }

    @Test
    public void isValid_shouldReturnTrue_whenUseDefaultRegexAndObjectMatchDefaultRegex() throws NoSuchFieldException {
        // GIVEN instance
        String object = "something@somewhere.com";
        ValidEmailAddressValidator validator = new ValidEmailAddressValidator(dynamicConfiguration);
        validator.initialize(givenAnnotation());

        // WHEN
        boolean result = validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(true));
    }


    @Test
    public void isValid_shouldReturnTrue_whenUseRegexAndObjectMatchesRegex() throws NoSuchFieldException {
        // GIVEN instance
        String object = "something@somewhere.com";
        ValidEmailAddressValidator validator = new ValidEmailAddressValidator(dynamicConfiguration);
        validator.initialize(givenAnnotationWithExpression());

        // WHEN
        boolean result = validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(true));
    }


    @Test
    public void isValid_shouldReturnFalse_whenUseDefaultRegexAndObjectNotMatchDefaultRegex() throws NoSuchFieldException {
        // GIVEN instance
        String object = "ABCD1345678";
        ValidEmailAddressValidator validator = new ValidEmailAddressValidator(dynamicConfiguration);
        validator.initialize(givenAnnotation());

        // WHEN
        boolean result = validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(false));
        verifyAddMesssageParameter("value", object);
    }

    @Test
    public void isValid_shouldReturnFalse_whenUseRegexAndObjectNotMatchRegex() throws NoSuchFieldException {
        // GIVEN instance
        String object = "abcdef.12345.com";
        ValidEmailAddressValidator validator = new ValidEmailAddressValidator(dynamicConfiguration);
        validator.initialize(givenAnnotationWithExpression());

        // WHEN
        boolean result =  validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(false));
        verifyAddMesssageParameter("value", object);
    }

    private static ValidEmailAddress givenAnnotation() throws NoSuchFieldException {
        final class ApiModel {
            @ValidEmailAddress
            private String s;
        }
        return ApiModel.class.getDeclaredField("s").getAnnotation(ValidEmailAddress.class);
    }
    
    private static ValidEmailAddress givenAnnotationWithExpression() throws NoSuchFieldException {
        final class ApiModel {
            @ValidEmailAddress(expression = "[^@]+@[^\\.]+\\..+")
            private String s;
        }
        return ApiModel.class.getDeclaredField("s").getAnnotation(ValidEmailAddress.class);
    }
}
