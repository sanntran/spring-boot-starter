package net.ionoff.common.validation.validator;

import net.ionoff.common.validation.constraints.AllNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AllNotNullTest extends AbstractConstraintValidatorTest {

    @AllNotNull(fields = "field1", dependOn = "field2", condition = "isNotNull")
    private static final class ApiModel1 {
        ApiModel1(Object field1, Object field2) {
            this.field1 = field1;
            this.field2 = field2;
        }
        private Object field1;
        private Object field2;
    }

    @AllNotNull(fields = "field1,field2")
    private static final class ApiModel2 {
        ApiModel2(Object field1, Object field2) {
            this.field1 = field1;
            this.field2 = field2;
        }
        private Object field1;
        private Object field2;
    }

    @Test
    public void isValid_shouldReturnFalse_whenAnyFieldIsNullWithoutDependOn() {
        // GIVEN instance
        ApiModel2 object = new ApiModel2(null, new Object());
        AllNotNullValidator validator = new AllNotNullValidator();
        validator.initialize(givenAnnotationWithoutDependOn());

        // WHEN
        boolean result = validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(false));
        verifyAddExpressionVariableFields(Arrays.asList("field1", "field2"));
    }

    @Test
    public void isValid_shouldReturnTrue_whenAllFieldsAreNotNullWithoutDependOn() {
        // GIVEN instance
        AllNotNullValidator validator = new AllNotNullValidator();
        validator.initialize(givenAnnotationWithoutDependOn());

        // WHEN
        boolean result =  validator.isValid(new ApiModel1(new Object(), new Object()), validatorContext);

        // THEN
        assertThat(result, equalTo(true));
    }

    @Test
    public void isValid_shouldReturnTrue_whenAllFieldsAreNotNullWithDependOn() {
        // GIVEN instance
        AllNotNullValidator validator = new AllNotNullValidator();
        validator.initialize(givenAnnotationWithDependOn());

        // WHEN
        boolean result =  validator.isValid(new ApiModel1(new Object(), new Object()), validatorContext);

        // THEN
        assertThat(result, equalTo(true));
    }

    @Test
    public void isValid_shouldReturnTrue_whenAnyFieldsIsNullWithDependOnNull() {
        // GIVEN instance
        AllNotNullValidator validator = new AllNotNullValidator();
        validator.initialize(givenAnnotationWithDependOn());

        // WHEN
        boolean result =  validator.isValid(new ApiModel1(null, null), validatorContext);

        // THEN
        assertThat(result, equalTo(true));
    }

    @Test
    public void isValid_shouldReturnTrue_whenAnyFieldsIsNullWithDependOnNotNull() {
        // GIVEN instance
        AllNotNullValidator validator = new AllNotNullValidator();
        validator.initialize(givenAnnotationWithDependOn());

        // WHEN
        boolean result =  validator.isValid(new ApiModel1(null, new Object()), validatorContext);

        // THEN
        assertThat(result, equalTo(false));
        verifyAddExpressionVariableFields(Arrays.asList("field1"));
    }

    private static AllNotNull givenAnnotationWithDependOn() {
        return ApiModel1.class.getAnnotation(AllNotNull.class);
    }
    private static AllNotNull givenAnnotationWithoutDependOn() {
        return ApiModel2.class.getAnnotation(AllNotNull.class);
    }
}
