package net.ionoff.common.validation.validator;

import net.ionoff.common.validation.constraints.AcceptedValue;
import net.ionoff.common.validation.expression.ExpressionParser;
import net.ionoff.common.validation.expression.IExpression;
import net.ionoff.common.validation.DynamicConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Component
public class AcceptedValueNumberValidator extends AbstractValidator
        implements ConstraintValidator<AcceptedValue, Number> {

    private Set<BigDecimal> values;
    private IExpression expression;
    private Set<BigDecimal> staticList;
    private DynamicConfiguration dynamicConfiguration;

    @Autowired
    public AcceptedValueNumberValidator(DynamicConfiguration dynamicConfiguration) {
        this.dynamicConfiguration = dynamicConfiguration;
    }

    @Override
    public void initialize(AcceptedValue constraintAnnotation) {
        values = new HashSet<>();
        for (String val : constraintAnnotation.value().split(",")) {
            if (!val.isEmpty()) {
                values.add(new BigDecimal(val));
            }
        }
        expression = ExpressionParser.parse(constraintAnnotation.expression());
        expression.setDynamicConfiguration(dynamicConfiguration);
        if (isExpression(expression) && expression.isStatic()) {
            staticList = expression.getAsBigDecimalSet();
        }
    }

    @Override
    public boolean isValid(Number value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        Set<BigDecimal> acceptedStringSet = getAcceptedNumberSet();
        if (acceptedStringSet.contains(new BigDecimal(value.toString()))) {
            return true;
        }
        addMessageParameter(context, "value", value);
        return false;
    }

    private Set<BigDecimal> getAcceptedNumberSet() {
        if (!isExpression(expression)) {
            return values;
        }
        if (expression.isStatic()) {
            return staticList;
        }
        return expression.getAsBigDecimalSet();
    }
}
