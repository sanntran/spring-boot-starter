package net.ionoff.common.validation.validator;

import net.ionoff.common.validation.constraints.ValidInteger;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidIntegerStringValidator extends AbstractValidator
        implements ConstraintValidator<ValidInteger, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (isValidInteger(value)) {
            return true;
        }
        addMessageParameter(context, "value", value);
        return false;
    }

    static boolean isValidInteger(String value) {
        if (value == null) {
            return true;
        }
        try {
            Integer.parseInt(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
