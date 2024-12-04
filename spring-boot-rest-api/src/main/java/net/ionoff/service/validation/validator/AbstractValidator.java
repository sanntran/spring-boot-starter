package net.ionoff.service.validation.validator;

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

import javax.validation.ConstraintValidatorContext;
import java.util.Collection;

public class AbstractValidator {

    protected static final boolean VALID = true;
    protected static final boolean NOT_VALID = false;
    protected static final boolean NOT_APPLICABLE = true;

    protected void addFieldsVariable(ConstraintValidatorContext context, Collection<String> fields) {
        context.unwrap(HibernateConstraintValidatorContext.class).addExpressionVariable("fields", fields);
    }

    /**
     * Be very careful and NEVER pass untrusted value (ex user input)
     * because this method will execute if trustedValue is an expression.
     * The message template should look like "... {value} ...".
     * @param trustedValue As the value can be an expression, it must be trusted
     */
    protected void addTrustedMessageParameter(ConstraintValidatorContext context, String param, Object trustedValue) {
        context.unwrap(HibernateConstraintValidatorContext.class).addMessageParameter(param, trustedValue);
    }

    /**
     * This method allow passing untrusted value (ex user input).
     * The message template should look like "... ${validatedValue} ...".
     * @param invalidValue Value of parameter, the value can be an expression.
     */
    protected void addInvalidMessageParameter(ConstraintValidatorContext context, String param, Object invalidValue) {
        addExpressionVariable(context, param, invalidValue);
    }

    /**
     * This method allow passing untrusted value (ex user input) without executing it
     * The message template should look like "... ${validatedValue} ...".
     * @param validatedValue Value of parameter.
     */
    protected void addExpressionVariable(ConstraintValidatorContext context, String param, Object validatedValue) {
        context.unwrap(HibernateConstraintValidatorContext.class).addExpressionVariable(param, validatedValue);
    }
}
