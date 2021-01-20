package net.ionoff.common.validation.validator;

import net.ionoff.common.validation.constraints.MaxValue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class MaxValueTest extends AbstractConstraintValidatorTest{

    @Before
    public void setup() {
        super.setup();
        when(dynamicConfiguration.getStringValue("config.key", "50"))
                .thenReturn("50");
    }


    @Test
    public void isValid_shouldReturnTrue_whenObjectIsNull() throws NoSuchFieldException {
        // GIVEN instance
        MaxValueValidator validator = new MaxValueValidator(dynamicConfiguration);
        validator.initialize(givenAnnotationWithValue());

        // WHEN
        boolean result =  validator.isValid(null, validatorContext);

        // THEN
        assertThat(result, equalTo(true));
    }

    @Test
    public void isValid_shouldReturnFalse_whenObjectNotValid() throws NoSuchFieldException {
        // GIVEN instance
        BigDecimal object = BigDecimal.valueOf(51);
        MaxValueValidator validator = new MaxValueValidator(dynamicConfiguration);
        validator.initialize(givenAnnotationWithValue());

        // WHEN
        boolean result = validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(false));
        verifyAddMesssageParameter("value", 50);
    }

    @Test
    public void isValid_shouldReturnTrue_whenObjectValid() throws NoSuchFieldException {
        // GIVEN instance
        BigDecimal object = BigDecimal.valueOf(10);
        MaxValueValidator validator = new MaxValueValidator(dynamicConfiguration);
        validator.initialize(givenAnnotationWithExpression());

        // WHEN
        boolean result = validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(true));
    }

    private static MaxValue givenAnnotationWithValue() throws NoSuchFieldException {
        final class ApiModel {
            @MaxValue(value = 50)
            private BigDecimal s;
        }
        return ApiModel.class.getDeclaredField("s").getAnnotation(MaxValue.class);
    }

    private static MaxValue givenAnnotationWithExpression() throws NoSuchFieldException {
        final class ApiModel {
            @MaxValue(expression = "$config::{config.key, 50}")
            private BigDecimal s;
        }
        return ApiModel.class.getDeclaredField("s").getAnnotation(MaxValue.class);
    }

}
