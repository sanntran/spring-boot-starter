package net.ionoff.common.validation.validator;

import net.ionoff.common.validation.constraints.ValidUUID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ValidUUIDArrayTest extends AbstractConstraintValidatorTest {


    @Test
    public void isValid_shouldReturnTrue_whenValueIsNull() throws NoSuchFieldException {
        // GIVEN instance
        List<String> object = null;
        ValidUUIDArrayValidator validator = new ValidUUIDArrayValidator();
        validator.initialize(givenAnnotation());

        // WHEN
        boolean result = validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(true));
    }

    @Test
    public void isValid_shouldReturnTrue_whenValueIsValidUUIDArray() throws NoSuchFieldException {
        // GIVEN instance
        List<String> object = Arrays.asList(UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString());
        ValidUUIDArrayValidator validator = new ValidUUIDArrayValidator();
        validator.initialize(givenAnnotation());

        // WHEN
        boolean result = validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(true));
    }

    @Test
    public void isValid_shouldReturnFalse_whenValueContainsNotUUIDItem() throws NoSuchFieldException {
        // GIVEN instance
        List<String> object = Arrays.asList("A", "B", UUID.randomUUID().toString());
        ValidUUIDArrayValidator validator = new ValidUUIDArrayValidator();
        validator.initialize(givenAnnotation());

        // WHEN
        boolean result = validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(false));
        verifyAddMesssageParameter("value", "A,B");
    }

    private static ValidUUID givenAnnotation() throws NoSuchFieldException {
        final class ApiModel {
            @ValidUUID
            private List<String> s;
        }
        return ApiModel.class.getDeclaredField("s").getAnnotation(ValidUUID.class);
    }
}
