package net.ionoff.common.validation.validator;

import net.ionoff.common.validation.constraints.AcceptedValue;
import net.ionoff.common.validation.expression.ExpressionParser;
import net.ionoff.common.validation.expression.IExpression;
import net.ionoff.common.validation.DynamicConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.*;

@Component
public class AcceptedValueCollectionValidator extends AbstractValidator
        implements ConstraintValidator<AcceptedValue, Collection<?>> {

    private Set<String> values;
    private IExpression expression;
    private Set<String> staticList;
    private DynamicConfiguration dynamicConfiguration;

    @Autowired
    public AcceptedValueCollectionValidator(DynamicConfiguration dynamicConfiguration) {
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
    public boolean isValid(Collection<?> object, ConstraintValidatorContext context) {
        if (object == null) {
            return true;
        }
        Set<String> acceptedStringSet = getAcceptedStringSet();
        List<String> notAcceptedValues = new ArrayList<>();
        for (Object value : object) {
            if (value != null && !acceptedStringSet.contains(value.toString())) {
                notAcceptedValues.add(value.toString());
            }
        }
        if (notAcceptedValues.isEmpty()) {
            return true;
        }
        addMessageParameter(context, "value", String.join(",", notAcceptedValues));
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
