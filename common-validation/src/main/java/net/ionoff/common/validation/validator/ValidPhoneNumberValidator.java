package net.ionoff.common.validation.validator;

import net.ionoff.common.validation.constraints.ValidPhoneNumber;
import net.ionoff.common.validation.expression.ExpressionParser;
import net.ionoff.common.validation.expression.IExpression;
import net.ionoff.common.validation.DynamicConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class ValidPhoneNumberValidator extends AbstractValidator
        implements ConstraintValidator<ValidPhoneNumber, String> {

    private String regex;
    private IExpression expression;
    private DynamicConfiguration dynamicConfiguration;

    @Autowired
    public ValidPhoneNumberValidator(DynamicConfiguration dynamicConfiguration) {
        this.dynamicConfiguration = dynamicConfiguration;
    }

    @Override
    public void initialize(ValidPhoneNumber constraintAnnotation) {
        regex = constraintAnnotation.regex();
        expression = ExpressionParser.parse(constraintAnnotation.expression());
        expression.setDynamicConfiguration(dynamicConfiguration);
    }

    @Override
    public boolean isValid(String object, ConstraintValidatorContext context) {
        if (isValidPhoneNumber(object)) {
            return true;
        }
        addMessageParameter(context, "value", String.valueOf(object));
        return false;
    }

    private boolean isValidPhoneNumber(String value) {
        if (value == null) {
            return true;
        }
        String phoneNumberRegex = !isExpression(expression) ? regex : expression.getAsString();
        if (phoneNumberRegex.isEmpty()) {
            return !value.trim().isEmpty();
        }
        return value.matches(phoneNumberRegex);
    }

}
