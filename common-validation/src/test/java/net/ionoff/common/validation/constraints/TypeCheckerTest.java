package net.ionoff.common.validation.constraints;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class TypeCheckerTest {

    @Test
    public void parseTypeChecker_shouldReturnTypeChecker_whenInputIsValid() {
        // GIVEN
        String propertyValue = "net.ionoff.common.validation.constraints.SampleTypeChecker";

        // WHEN
        TypeChecker typeChecker = TypeChecker.fromString(propertyValue);

        // THEN
        assertThat(typeChecker.getClass(), equalTo(SampleTypeChecker.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseTypeChecker_shouldThrowIllegalArgumentException_whenInputIsNotValid() {
        // GIVEN
        String propertyValue = "not a valid TypeChecker class";

        // WHEN
        TypeChecker.fromString(propertyValue);

        // THEN exception should be thrown as expected
    }
}
