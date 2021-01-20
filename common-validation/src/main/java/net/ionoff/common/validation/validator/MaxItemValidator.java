package net.ionoff.common.validation.validator;

import net.ionoff.common.validation.constraints.MaxItem;
import net.ionoff.common.validation.constraints.TypeChecker;
import net.ionoff.common.validation.expression.ExpressionParser;
import net.ionoff.common.validation.expression.IExpression;
import net.ionoff.common.validation.DynamicConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;

@Component
public class MaxItemValidator extends AbstractValidator
        implements ConstraintValidator<MaxItem, Collection<?>> {

    private int value;
    private TypeChecker type;
    private IExpression expression;
    private DynamicConfiguration dynamicConfiguration;

    @Autowired
    public MaxItemValidator(DynamicConfiguration dynamicConfiguration) {
        this.dynamicConfiguration = dynamicConfiguration;
    }

    @Override
    public void initialize(MaxItem constraintAnnotation) {
        value = constraintAnnotation.value();
        expression = ExpressionParser.parse(constraintAnnotation.expression());
        expression.setDynamicConfiguration(dynamicConfiguration);
        String typeChecker = constraintAnnotation.type();
        if (!typeChecker.isEmpty()) {
            type = TypeChecker.fromString(typeChecker);
        }
    }

    @Override
    public boolean isValid(Collection<?> object, ConstraintValidatorContext context) {
        if (object == null) {
            return true;
        }
        int itemCount = ValidatorSupport.countItem(object, type);
        int maxItem = !isExpression(expression) ? value : expression.getAsInteger();
        if (itemCount <= maxItem) {
            return true;
        }
        addMessageParameter(context, "value", maxItem);
        if (type != null) {
            addMessageParameter(context, "type", type.getType());
        }
        return false;
    }
}
