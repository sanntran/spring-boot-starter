package net.ionoff.common.validation.validator;

import net.ionoff.common.validation.constraints.MaxLength;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class MaxLengthTest extends AbstractConstraintValidatorTest {

    @Before
    public void setup() {
        super.setup();
        when(dynamicConfiguration.getStringValue("config.key", "50"))
                .thenReturn("50");
    }


    @Test
    public void isValid_shouldReturnTrue_whenObjectIsNull() throws NoSuchFieldException {
        // GIVEN instance
        MaxLengthValidator validator = new MaxLengthValidator(dynamicConfiguration);
        validator.initialize(givenAnnotationWithValue());

        // WHEN
        boolean result =  validator.isValid(null, validatorContext);

        // THEN
        assertThat(result, equalTo(true));
    }

    @Test
    public void isValid_shouldReturnFalse_whenObjectNotValid() throws NoSuchFieldException {
        // GIVEN instance
        MaxLengthValidator validator = new MaxLengthValidator(dynamicConfiguration);
        validator.initialize(givenAnnotationWithValue());

        String object = "StringLengthMoreThan10StringLengthMoreThan10StringLengthMoreThan10StringLengthMoreThan10";

        // WHEN
        boolean result = validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(false));
        verifyAddMesssageParameter("value", 50);
    }

    @Test
    public void isValid_shouldReturnTrue_whenObjectValid() throws NoSuchFieldException {
        // GIVEN instance
        MaxLengthValidator validator = new MaxLengthValidator(dynamicConfiguration);
        validator.initialize(givenAnnotationWithExpression());

        String object = "StringLengtLessThan50";

        // WHEN
        boolean result = validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(true));
    }


    private static MaxLength givenAnnotationWithValue() throws NoSuchFieldException {
        final class ApiModel {
            @MaxLength(value = 50)
            private String s;
        }
        return ApiModel.class.getDeclaredField("s").getAnnotation(MaxLength.class);
    }

    private static MaxLength givenAnnotationWithExpression() throws NoSuchFieldException {
        final class ApiModel {
            @MaxLength(expression = "$config::{config.key, 50}")
            private String s;
        }
        return ApiModel.class.getDeclaredField("s").getAnnotation(MaxLength.class);
    }

}
