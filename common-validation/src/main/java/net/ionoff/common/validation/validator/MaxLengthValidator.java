package net.ionoff.common.validation.validator;

import net.ionoff.common.validation.constraints.MaxLength;
import net.ionoff.common.validation.expression.ExpressionParser;
import net.ionoff.common.validation.expression.IExpression;
import net.ionoff.common.validation.DynamicConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MaxLengthValidator extends AbstractValidator
        implements ConstraintValidator<MaxLength, String> {

    private int vatlue;
    private IExpression expression;
    private DynamicConfiguration dynamicConfiguration;

    @Autowired
    public MaxLengthValidator(DynamicConfiguration dynamicConfiguration) {
        this.dynamicConfiguration = dynamicConfiguration;
    }


    @Override
    public void initialize(MaxLength constraintAnnotation) {
        vatlue = constraintAnnotation.value();
        expression = ExpressionParser.parse(constraintAnnotation.expression());
        expression.setDynamicConfiguration(dynamicConfiguration);
    }

    @Override
    public boolean isValid(String string, ConstraintValidatorContext context) {
        int maxLength = !isExpression(expression) ? vatlue : expression.getAsInteger();
        int stringLength = string == null ? -1 : string.length();
        if (stringLength <= maxLength) {
            return true;
        }
        addMessageParameter(context, "value", maxLength);
        return false;
    }
}
