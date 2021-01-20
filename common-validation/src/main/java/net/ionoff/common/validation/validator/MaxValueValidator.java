package net.ionoff.common.validation.validator;

import net.ionoff.common.validation.constraints.MaxValue;
import net.ionoff.common.validation.expression.ExpressionParser;
import net.ionoff.common.validation.expression.IExpression;
import net.ionoff.common.validation.DynamicConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

@Component
public class MaxValueValidator extends AbstractValidator
        implements ConstraintValidator<MaxValue, Number> {

    private int value;
    private IExpression expression;
    private DynamicConfiguration dynamicConfiguration;

    @Autowired
    public MaxValueValidator(DynamicConfiguration dynamicConfiguration) {
        this.dynamicConfiguration = dynamicConfiguration;
    }


    @Override
    public void initialize(MaxValue constraintAnnotation) {
        value = constraintAnnotation.value();
        expression = ExpressionParser.parse(constraintAnnotation.expression());
        expression.setDynamicConfiguration(dynamicConfiguration);
    }

    @Override
    public boolean isValid(Number object, ConstraintValidatorContext context) {
        if (object == null) {
            return true;
        }
        int maxValue = !isExpression(expression) ? value : expression.getAsInteger();
        if (new BigDecimal(object.toString()).compareTo(BigDecimal.valueOf(maxValue)) <= 0) {
            return true;
        }
        addMessageParameter(context, "value", maxValue);
        return false;
    }

}
