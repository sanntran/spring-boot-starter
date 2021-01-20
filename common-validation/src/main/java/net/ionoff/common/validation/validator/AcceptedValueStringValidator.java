package net.ionoff.common.validation.validator;

import net.ionoff.common.validation.constraints.AcceptedValue;
import net.ionoff.common.validation.expression.ExpressionParser;
import net.ionoff.common.validation.expression.IExpression;
import net.ionoff.common.validation.DynamicConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class AcceptedValueStringValidator extends AbstractValidator
        implements ConstraintValidator<AcceptedValue, String> {

    private Set<String> values;
    private IExpression expression;
    private Set<String> staticList;
    private DynamicConfiguration dynamicConfiguration;

    @Autowired
    public AcceptedValueStringValidator(DynamicConfiguration dynamicConfiguration) {
        this.dynamicConfiguration = dynamicConfiguration;
    }

    @Override
    public void initialize(AcceptedValue constraintAnnotation) {
        values = new HashSet<>();
        values.addAll(Arrays.asList(constraintAnnotation.value().split(",")));
        expression = ExpressionParser.parse(constraintAnnotation.expression());
        expression.setDynamicConfiguration(dynamicConfiguration);
        if (isExpression(expression) && expression.isStatic()) {
            staticList = expression.getAsStringSet();
        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Set<String> acceptedStringSet = getAcceptedStringSet();
        if (value == null || acceptedStringSet.contains(value)) {
            return true;
        }
        addMessageParameter(context, "value", value);
        return false;
    }

    private Set<String> getAcceptedStringSet() {
        if (!isExpression(expression)) {
            return values;
        }
        if (expression.isStatic()) {
            return staticList;
        }
        return expression.getAsStringSet();
    }
}
