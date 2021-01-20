package net.ionoff.common.validation.validator;

import net.ionoff.common.validation.constraints.ValidUnsignedInteger;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidUnsignedStringValidator extends AbstractValidator
        implements ConstraintValidator<ValidUnsignedInteger, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (isValidUnsignedInteger(value)) {
            return true;
        }
        addMessageParameter(context, "value", value);
        return false;
    }

    static boolean isValidUnsignedInteger(String value) {
        if (value == null) {
            return true;
        }
        try {
            Integer.parseUnsignedInt(value);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
