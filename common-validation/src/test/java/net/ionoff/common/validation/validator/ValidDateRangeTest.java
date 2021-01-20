package net.ionoff.common.validation.validator;

import net.ionoff.common.validation.constraints.ValidDateRange;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.OffsetDateTime;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ValidDateRangeTest extends AbstractConstraintValidatorTest {
    @ValidDateRange(fromDate = "fromDate", toDate = "toDate")
    private static final class ApiModel {

        ApiModel(OffsetDateTime fromDate, OffsetDateTime toDate) {
            this.fromDate = fromDate;
            this.toDate = toDate;
        }
        private OffsetDateTime fromDate;
        private OffsetDateTime toDate;

    }
    @Test
    public void isValid_shouldReturnTrue_whenApiModelIsNull() {
        // GIVEN instance
        ValidDateRangeValidator validator = new ValidDateRangeValidator();
        validator.initialize(givenAnnotation());

        // WHEN
        boolean result = validator.isValid(null, validatorContext);

        // THEN
        assertThat(result, equalTo(true));
    }

    @Test
    public void isValid_shouldReturnFalse_whenFromDateIsNotBeforeToDate() {
        // GIVEN instance
        ValidDateRangeValidator validator = new ValidDateRangeValidator();
        validator.initialize(givenAnnotation());

        ApiModel object = new ApiModel(OffsetDateTime.parse("2020-02-20T12:00Z"), OffsetDateTime.parse("2020-01-20T12:00Z"));

        // WHEN
        boolean result = validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(false));
        verify(hibernateValidatorContext, times(1))
                                                .addExpressionVariable("fields", Arrays.asList("fromDate", "toDate"));
    }

    @Test
    public void isValid_shouldReturnTrue_whenAnyDateToCompareIsNull() {
        // GIVEN instance
        ValidDateRangeValidator validator = new ValidDateRangeValidator();
        validator.initialize(givenAnnotation());

        ApiModel object = new ApiModel(null, OffsetDateTime.parse("2020-01-20T12:00Z"));

        // WHEN
        boolean result = validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(true));
    }

    @Test
    public void isValid_shouldReturnTrue_whenFromDateIsBeforeToDate() {
        ValidDateRangeValidator validator = new ValidDateRangeValidator();
        validator.initialize(givenAnnotation());

        ApiModel object = new ApiModel(OffsetDateTime.parse("2020-01-20T12:00Z"), OffsetDateTime.parse("2020-02-20T12:00Z"));

        // WHEN
        boolean result = validator.isValid(object, validatorContext);

        // THEN
        assertThat(result, equalTo(true));
    }


    private static ValidDateRange givenAnnotation() {
        return ApiModel.class.getAnnotation(ValidDateRange.class);
    }
}
