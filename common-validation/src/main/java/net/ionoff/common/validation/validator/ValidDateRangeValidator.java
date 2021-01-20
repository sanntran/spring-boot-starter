package net.ionoff.common.validation.validator;

import net.ionoff.common.validation.constraints.ValidDateRange;
import net.ionoff.common.validation.exception.GetFieldValueException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.time.OffsetDateTime;
import java.util.Arrays;

public class ValidDateRangeValidator extends AbstractValidator
        implements ConstraintValidator<ValidDateRange, Object> {

    private String fromDate;
    private String toDate;

    @Override
    public void initialize(ValidDateRange constraintAnnotation) {
        fromDate = constraintAnnotation.fromDate();
        toDate = constraintAnnotation.toDate();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        if (object == null) {
            return true;
        }
        try {
            Field fromDateField = object.getClass().getDeclaredField(fromDate);
            fromDateField.setAccessible(true);
            Field toDateField = object.getClass().getDeclaredField(toDate);
            toDateField.setAccessible(true);
            OffsetDateTime fromDateFieldValue = (OffsetDateTime) fromDateField.get(object);
            OffsetDateTime toDateFieldValue = (OffsetDateTime) toDateField.get(object);
            if (fromDateFieldValue == null || toDateFieldValue == null || fromDateFieldValue.isBefore(toDateFieldValue)) {
                return true;
            }
            addFieldsVariable(context, Arrays.asList(fromDate, toDate));
            return false;
        } catch (NoSuchFieldException | IllegalAccessException | ClassCastException e) {
            throw new GetFieldValueException(String.format("Error when getting value of field '%s,%s' from model '%s'",
                    fromDate, toDate, object.getClass().getCanonicalName()));
        }
    }
}
