package net.ionoff.common.validation.validator;

import net.ionoff.common.validation.constraints.ValidUUID;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class ValidUUIDArrayValidator extends AbstractValidator
        implements ConstraintValidator<ValidUUID, Collection<?>> {

    boolean acceptNull = false;

    @Override
    public void initialize(ValidUUID constraintAnnotation) {
        acceptNull = constraintAnnotation.acceptNull();
    }

    @Override
    public boolean isValid(Collection<?> collection, ConstraintValidatorContext context) {
        if (collection == null || collection.isEmpty()) {
            return true;
        }
        List<String> invalidUUIds = getInvalidUUIds(collection);
        if (invalidUUIds.isEmpty()) {
            return true;
        }
        addMessageParameter(context, "value", String.join(",", invalidUUIds));
        return false;
    }

    private List<String> getInvalidUUIds(Collection<?> collection) {
        List<String> invalidUUIds = new ArrayList<>();
        for (Object value : collection) {
            if (value == null && !acceptNull) {
                invalidUUIds.add("null");
            } else if (value != null) {
                try {
                    UUID.fromString(value.toString());
                } catch (Exception e) {
                    invalidUUIds.add(String.valueOf(value));
                }
            }
        }
        return invalidUUIds;
    }
}
