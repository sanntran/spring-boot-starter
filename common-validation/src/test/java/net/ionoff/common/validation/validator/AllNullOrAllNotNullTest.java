package net.ionoff.common.validation.validator;

import net.ionoff.common.validation.constraints.AllNullOrAllNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AllNullOrAllNotNullTest extends AbstractConstraintValidatorTest {

    @AllNullOrAllNotNull(fields = "field1,field2,field3")
    private static final class ApiModel {
        ApiModel(Object field1, Object field2, Object field3) {
            this.field1 = field1;
            this.field2 = field2;
            this.field3 = field3;
        }
        private Object field1;
        private Object field2;
        private Object field3;
    }

    @Test
    public void isValid_shouldReturnFalse_whenOneFieldsAreNotNull() {
        // GIVEN instance
        ApiModel object = new ApiModel(null, null, new Object());
        AllNullOrAllNotNullValidator validator = new AllNullOrAllNotNullValidator();
        validator.initialize(givenAnnotation());

        // WHEN
        boolean result = validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(false));
        verifyAddExpressionVariableFields(Arrays.asList("field1", "field2", "field3"));
    }

    @Test
    public void isValid_shouldReturnTrue_whenAllFieldsAreNull() {
        // GIVEN instance
        AllNullOrAllNotNullValidator validator = new AllNullOrAllNotNullValidator();
        validator.initialize(givenAnnotation());

        // WHEN
        boolean result =  validator.isValid(new ApiModel(null, null, null), validatorContext);

        // THEN
        assertThat(result, equalTo(true));
    }
    @Test
    public void isValid_shouldReturnTrue_whenAllFieldsAreNotNull() {
        // GIVEN instance
        AllNullOrAllNotNullValidator validator = new AllNullOrAllNotNullValidator();
        validator.initialize(givenAnnotation());

        // WHEN
        boolean result =  validator.isValid(new ApiModel(new Object(), new Object(), new Object()), validatorContext);

        // THEN
        assertThat(result, equalTo(true));
    }


    private static AllNullOrAllNotNull givenAnnotation() {
        return ApiModel.class.getAnnotation(AllNullOrAllNotNull.class);
    }
}
