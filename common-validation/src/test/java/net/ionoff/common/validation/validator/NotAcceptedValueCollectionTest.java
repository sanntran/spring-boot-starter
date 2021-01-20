package net.ionoff.common.validation.validator;

import net.ionoff.common.validation.constraints.NotAcceptedValue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.Silent.class)
public class NotAcceptedValueCollectionTest extends AbstractConstraintValidatorTest{

    @Test
    public void isValid_shouldReturnTrue_whenObjectIsNull() throws NoSuchFieldException {
        // GIVEN instance
        NotAcceptedValueCollectionValidator validator = new NotAcceptedValueCollectionValidator(dynamicConfiguration);
        validator.initialize(givenAnnotationWithValue());

        // WHEN
        boolean result =  validator.isValid(null, validatorContext);

        // THEN
        assertThat(result, equalTo(true));
    }

    @Test
    public void isValid_shouldReturnFalse_whenValueIsInNotAcceptedValueList() throws NoSuchFieldException {
        // GIVEN instance
        NotAcceptedValueCollectionValidator validator = new NotAcceptedValueCollectionValidator(dynamicConfiguration);
        validator.initialize(givenAnnotationWithValue());

        List<String> object = Arrays.asList("X", "Y");

        // WHEN
        boolean result = validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(false));
        verifyAddMesssageParameter("value", "X,Y");
    }

    @Test
    public void isValid_shouldReturnTrue_whenValueIsNotInNotAcceptedValueList() throws NoSuchFieldException {
        // GIVEN instance
        NotAcceptedValueCollectionValidator validator = new NotAcceptedValueCollectionValidator(dynamicConfiguration);
        validator.initialize(givenAnnotationWithExpression());

        List<String> object = Arrays.asList("A", "B");

        // WHEN
        boolean result = validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(true));
    }

    private static NotAcceptedValue givenAnnotationWithValue() throws NoSuchFieldException {
        final class ApiModel {
            @NotAcceptedValue(value = "X,Y,Z")
            private String s;
        }
        return ApiModel.class.getDeclaredField("s").getAnnotation(NotAcceptedValue.class);
    }

    private static NotAcceptedValue givenAnnotationWithExpression() throws NoSuchFieldException {
        final class ApiModel {
            @NotAcceptedValue(expression = "$reflection::net.ionoff.common.validation.constant.Reflections#notAcceptedStrings")
            private String s;
        }
        return ApiModel.class.getDeclaredField("s").getAnnotation(NotAcceptedValue.class);
    }
}
