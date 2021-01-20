package net.ionoff.common.validation.validator;

import net.ionoff.common.validation.constraints.ValidNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class ValidNumberValidator extends AbstractValidator
        implements ConstraintValidator<ValidNumber, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (isValidNumber(value)) {
            return true;
        }
        addMessageParameter(context, "value", value);
        return false;
    }

    public static boolean isValidNumber(String value) {
        if (value == null) {
            return true;
        }
        try {
            new BigDecimal(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
