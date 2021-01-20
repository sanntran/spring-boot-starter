package net.ionoff.common.validation.validator;

import net.ionoff.common.validation.constraints.ValidTimeZone;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.ZoneId;

public class ValidTimeZoneValidator extends AbstractValidator
        implements ConstraintValidator<ValidTimeZone, String> {

    @Override
    public boolean isValid(String object, ConstraintValidatorContext context) {
        if (object == null) {
            return true;
        }
        try {
            ZoneId.of(object);
            return true;
        } catch (Exception e) {
            addMessageParameter(context, "value", object);
            return false;
        }
    }
}
