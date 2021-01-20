package net.ionoff.common.validation.validator;

import net.ionoff.common.validation.expression.IExpression;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

import javax.validation.ConstraintValidatorContext;
import java.util.Collection;

public class AbstractValidator {

    protected void addFieldsVariable(ConstraintValidatorContext context, Collection<String> fields) {
        context.unwrap(HibernateConstraintValidatorContext.class).addExpressionVariable("fields", fields);
    }

    protected void addMessageParameter(ConstraintValidatorContext context, String param, Object value) {
        context.unwrap(HibernateConstraintValidatorContext.class).addMessageParameter(param, value);
    }

    protected boolean isExpression(IExpression expression) {
        return !"".equals(expression.get());
    }
}
