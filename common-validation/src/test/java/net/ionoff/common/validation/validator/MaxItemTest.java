package net.ionoff.common.validation.validator;

import net.ionoff.common.validation.constraints.MaxItem;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class MaxItemTest extends AbstractConstraintValidatorTest {

    @Before
    public void setup() {
        super.setup();
        when(dynamicConfiguration.getStringValue("config.key", "1"))
                .thenReturn("1");
    }

    @Test
    public void isValid_shouldReturnTrue_whenObjectIsNull() throws NoSuchFieldException {
        // GIVEN instance
        MaxItemValidator validator = new MaxItemValidator(dynamicConfiguration);
        validator.initialize(givenAnnotationWithValue());

        // WHEN
        boolean result =  validator.isValid(null, validatorContext);

        // THEN
        assertThat(result, equalTo(true));
    }

    @Test
    public void isValid_shouldReturnFalse_whenObjectIsInValid() throws NoSuchFieldException {
        // GIVEN instance
        MaxItemValidator validator = new MaxItemValidator(dynamicConfiguration);
        validator.initialize(givenAnnotationWithValue());
        
        List<Object> object = Arrays.asList(1, 3);

        // WHEN
        boolean result =  validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(false));
        verifyAddMesssageParameter("value", 1);
    }

    @Test
    public void isValid_shouldReturnFalse_whenObjectIsInValidUseTypeChecker() throws NoSuchFieldException {
        // GIVEN a instance
        MaxItemValidator validator = new MaxItemValidator(dynamicConfiguration);
        validator.initialize(givenAnnotationWithValueAndType());

        List<Object> object = Arrays.asList("Sample", "Sample");

        // WHEN
        boolean result =  validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(false));
        verifyAddMesssageParameter("value", 1);
        verifyAddMesssageParameter("type", "sample");
    }

    @Test
    public void isValid_shouldReturnTrue_whenObjectIsValid() throws NoSuchFieldException {
        // GIVEN instance
        MaxItemValidator validator = new MaxItemValidator(dynamicConfiguration);
        validator.initialize(givenAnnotationWithExpression());

        List<Object> object = Arrays.asList(new Object());

        // WHEN
        boolean result =  validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(true));
    }

    @Test
    public void isValid_shouldReturnTrue_whenObjectIsValidUseTypeChecker() throws NoSuchFieldException {
        // GIVEN a instance
        MaxItemValidator validator = new MaxItemValidator(dynamicConfiguration);
        validator.initialize(givenAnnotationWithExpressionAndType());

        List<Object> object = Arrays.asList("123455", "ABCD");

        // WHEN
        boolean result =  validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(true));
    }

    private static MaxItem givenAnnotationWithValue() throws NoSuchFieldException {
        final class ApiModel {
            @MaxItem(value = 1)
            private List<String> s;
        }
        return ApiModel.class.getDeclaredField("s").getAnnotation(MaxItem.class);
    }

    private static MaxItem givenAnnotationWithValueAndType() throws NoSuchFieldException {
        final class ApiModel {
            @MaxItem(value = 1, type = "net.ionoff.common.validation.constraints.SampleTypeChecker")
            private List<String> s;
        }
        return ApiModel.class.getDeclaredField("s").getAnnotation(MaxItem.class);
    }

    private static MaxItem givenAnnotationWithExpression() throws NoSuchFieldException {
        final class ApiModel {
            @MaxItem(expression = "$config::{config.key, 1}")
            private List<String> s;
        }
        return ApiModel.class.getDeclaredField("s").getAnnotation(MaxItem.class);
    }

    private static MaxItem givenAnnotationWithExpressionAndType() throws NoSuchFieldException {
        final class ApiModel {
            @MaxItem(expression = "$config::{config.key, 1}", type = "net.ionoff.common.validation.constraints.SampleTypeChecker")
            private List<String> s;
        }
        return ApiModel.class.getDeclaredField("s").getAnnotation(MaxItem.class);
    }
}
