package net.ionoff.common.validation.validator;

import net.ionoff.common.validation.constraints.NotAllNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.Silent.class)
public class NotAllNullTest extends AbstractConstraintValidatorTest {

    @NotAllNull(fields = "field1,field2")
    private static final class ApiModel {
        ApiModel(Object field1, Object field2) {
            this.field1 = field1;
            this.field2 = field2;
        }
        private Object field1;
        private Object field2;
    }
    
    @Test
    public void isValid_shouldReturnFalse_whenAllFieldAreNull() {
        // GIVEN instance
        ApiModel object = new ApiModel(null, null);
        NotAllNullValidator validator = new NotAllNullValidator();
        validator.initialize(givenAnnotation());

        // WHEN
        boolean result = validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(false));
        verifyAddExpressionVariableFields(Arrays.asList("field1", "field2"));
    }

    @Test
    public void isValid_shouldReturnTrue_whenAnyFieldsIsNotNull() {
        // GIVEN instance
        NotAllNullValidator validator = new NotAllNullValidator();
        validator.initialize(givenAnnotation());

        // WHEN
        boolean result =  validator.isValid(new ApiModel(null, new Object()), validatorContext);

        // THEN
        assertThat(result, equalTo(true));
    }

    @Test
    public void isValid_shouldReturnTrue_whenObjectIsNull() {
        // GIVEN instance
        NotAllNullValidator validator = new NotAllNullValidator();
        validator.initialize(givenAnnotation());

        // WHEN
        boolean result =  validator.isValid(null, validatorContext);

        // THEN
        assertThat(result, equalTo(true));
    }



    private static NotAllNull givenAnnotation() {
        return ApiModel.class.getAnnotation(NotAllNull.class);
    }
}
