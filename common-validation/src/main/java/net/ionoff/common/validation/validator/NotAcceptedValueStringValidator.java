package net.ionoff.common.validation.validator;

import net.ionoff.common.validation.constraints.NotAcceptedValue;
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
public class NotAcceptedValueStringValidator extends AbstractValidator
        implements ConstraintValidator<NotAcceptedValue, String> {

    private Set<String> values;
    private IExpression expression;
    private Set<String> staticList;
    private DynamicConfiguration dynamicConfiguration;

    @Autowired
    public NotAcceptedValueStringValidator(DynamicConfiguration dynamicConfiguration) {
        this.dynamicConfiguration = dynamicConfiguration;
    }

    @Override
    public void initialize(NotAcceptedValue constraintAnnotation) {
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
        if (value == null) {
            return true;
        }
        Set<String> notAcceptedStringSet = getNotAcceptedStringSet();
        if (!notAcceptedStringSet.contains(value)) {
            return true;
        }
        addMessageParameter(context, "value", value);
        return false;
    }

    private Set<String> getNotAcceptedStringSet() {
        if (!isExpression(expression)) {
            return values;
        }
        if (expression.isStatic()) {
            return staticList;
        }
        return expression.getAsStringSet();
    }
}
