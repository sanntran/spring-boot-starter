package net.ionoff.common.validation.validator;

import net.ionoff.common.validation.constraints.ValidUUID;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidUUIDValidator extends AbstractValidator
        implements ConstraintValidator<ValidUUID, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (ValidatorSupport.isValidUUID(value)) {
            return true;
        }
        addMessageParameter(context, "value", value);
        return false;
    }
}
