package net.ionoff.service.error.validator;

import org.hibernate.validator.internal.engine.ConstraintViolationImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintViolation;
import javax.validation.metadata.ConstraintDescriptor;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public interface CustomValidator<A extends Annotation, T> extends ConstraintValidator<A, T> {

    Map<String, CustomValidator> CUSTOM_VALIDATOR_MAP = new HashMap<>();

    default void register(CustomValidator validator) {
        CUSTOM_VALIDATOR_MAP.putIfAbsent(validator.getClass().toString(), validator);
    }

    default String enrichMessage(T invalidValue, String defaultMessage) {
        return defaultMessage;
    }

    static String enrichMessage(ConstraintViolation constraintViolation) {
        String defaultMessage = constraintViolation.getMessage();
        ConstraintDescriptor constraintDescriptor = constraintViolation.getConstraintDescriptor();
        for (Object clazz : constraintDescriptor.getConstraintValidatorClasses()) {
            CustomValidator customValidator = CUSTOM_VALIDATOR_MAP.get(clazz.toString());
            if (customValidator != null) {
                Object invalidValue = constraintViolation.getInvalidValue();
                return customValidator.enrichMessage(invalidValue, defaultMessage);
            }
        }
        ConstraintViolationImpl constraintViolationImpl = (ConstraintViolationImpl) constraintViolation;
        return defaultMessage;
    }
}
