package net.ionoff.common.validation.validator;

import net.ionoff.common.validation.constraints.NotAcceptedValue;
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
public class NotAcceptedValueNumberValidator extends AbstractValidator
                    implements ConstraintValidator<NotAcceptedValue, Number> {

    private Set<BigDecimal> values;
    private IExpression expression;
    private Set<BigDecimal> staticList;
    private DynamicConfiguration dynamicConfiguration;

    @Autowired
    public NotAcceptedValueNumberValidator(DynamicConfiguration dynamicConfiguration) {
        this.dynamicConfiguration = dynamicConfiguration;
    }

    @Override
    public void initialize(NotAcceptedValue constraintAnnotation) {
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
    public boolean isValid(Number object, ConstraintValidatorContext context) {
        if (object == null) {
            return true;
        }
        Set<BigDecimal> notAcceptedNumberList = getNotAcceptedNumberSet();
        if (!notAcceptedNumberList.contains(new BigDecimal(object.toString()))) {
            return true;
        }
        addMessageParameter(context, "value", object);
        return false;
    }

    private Set<BigDecimal> getNotAcceptedNumberSet() {
        if (!isExpression(expression)) {
            return values;
        }
        if (expression.isStatic()) {
            return staticList;
        }
        return expression.getAsBigDecimalSet();
    }
}
