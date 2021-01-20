package net.ionoff.common.validation.validator;

import net.ionoff.common.validation.constraints.ValidEmailAddress;
import net.ionoff.common.validation.expression.ExpressionParser;
import net.ionoff.common.validation.expression.IExpression;
import net.ionoff.common.validation.DynamicConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class ValidEmailAddressValidator extends AbstractValidator
        implements ConstraintValidator<ValidEmailAddress, String> {

    private String regex;
    private IExpression expression;
    private DynamicConfiguration dynamicConfiguration;

    @Autowired
    public ValidEmailAddressValidator(DynamicConfiguration dynamicConfiguration) {
        this.dynamicConfiguration = dynamicConfiguration;
    }

    @Override
    public void initialize(ValidEmailAddress constraintAnnotation) {
        regex = constraintAnnotation.regex();
        expression = ExpressionParser.parse(constraintAnnotation.regex());
        expression.setDynamicConfiguration(dynamicConfiguration);
    }

    @Override
    public boolean isValid(String object, ConstraintValidatorContext context) {
        if (isValidEmail(object)) {
            return true;
        }
        addMessageParameter(context, "value", String.valueOf(object));
        return false;
    }

    boolean isValidEmail(String object) {
        if (object == null) {
            return true;
        }
        String emailRegrex = !isExpression(expression) ? regex : expression.getAsString();
        return object.matches(emailRegrex);
    }

}
