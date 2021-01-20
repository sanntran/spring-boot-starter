package net.ionoff.common.validation.validator;

import net.ionoff.common.validation.constraints.MinItem;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class MinItemTest extends AbstractConstraintValidatorTest {

    @Before
    public void setup() {
        super.setup();
        when(dynamicConfiguration.getStringValue("config.key", "1"))
                .thenReturn("1");
    }

    @Test
    public void isValid_shouldReturnTrue_whenObjectIsNull() throws NoSuchFieldException {
        // GIVEN instance
        MinItemValidator validator = new MinItemValidator(dynamicConfiguration);
        validator.initialize(givenAnnotationWithValue());

        // WHEN
        boolean result =  validator.isValid(null, validatorContext);

        // THEN
        assertThat(result, equalTo(true));
    }

    @Test
    public void isValid_shouldReturnFalse_whenObjectIsInValid() throws NoSuchFieldException {
        // GIVEN instance
        MinItemValidator validator = new MinItemValidator(dynamicConfiguration);
        validator.initialize(givenAnnotationWithValue());

        List<Object> object = Collections.emptyList();

        // WHEN
        boolean result =  validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(false));
        verifyAddMesssageParameter("value", 1);
    }

    @Test
    public void isValid_shouldReturnFalse_whenObjectIsInValidUseTypeChecker() throws NoSuchFieldException {
        // GIVEN a instance
        MinItemValidator validator = new MinItemValidator(dynamicConfiguration);
        validator.initialize(givenAnnotationWithValueAndType());

        List<Object> object = Arrays.asList("A", "B");

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
        MinItemValidator validator = new MinItemValidator(dynamicConfiguration);
        validator.initialize(givenAnnotationWithExpression());

        List<Object> object = Arrays.asList("Sample", "Sample");

        // WHEN
        boolean result =  validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(true));
    }

    @Test
    public void isValid_shouldReturnTrue_whenObjectIsValidUseTypeChecker() throws NoSuchFieldException {
        // GIVEN a instance
        MinItemValidator validator = new MinItemValidator(dynamicConfiguration);
        validator.initialize(givenAnnotationWithExpressionAndType());

        List<Object> object = Arrays.asList("Sample", "Sample");

        // WHEN
        boolean result =  validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(true));
    }

    private static MinItem givenAnnotationWithValue() throws NoSuchFieldException {
        final class ApiModel {
            @MinItem(value = 1)
            private List<String> s;
        }
        return ApiModel.class.getDeclaredField("s").getAnnotation(MinItem.class);
    }

    private static MinItem givenAnnotationWithValueAndType() throws NoSuchFieldException {
        final class ApiModel {
            @MinItem(value = 1, type = "net.ionoff.common.validation.constraints.SampleTypeChecker")
            private List<String> s;
        }
        return ApiModel.class.getDeclaredField("s").getAnnotation(MinItem.class);
    }

    private static MinItem givenAnnotationWithExpression() throws NoSuchFieldException {
        final class ApiModel {
            @MinItem(expression = "$config::{config.key, 1}")
            private String s;
        }
        return ApiModel.class.getDeclaredField("s").getAnnotation(MinItem.class);
    }

    private static MinItem givenAnnotationWithExpressionAndType() throws NoSuchFieldException {
        final class ApiModel {
            @MinItem(expression = "$config::{config.key, 1}", type = "net.ionoff.common.validation.constraints.SampleTypeChecker")
            private String s;
        }
        return ApiModel.class.getDeclaredField("s").getAnnotation(MinItem.class);
    }
}
