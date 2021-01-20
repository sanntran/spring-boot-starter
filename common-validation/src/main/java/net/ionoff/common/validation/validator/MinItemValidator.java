package net.ionoff.common.validation.validator;

import net.ionoff.common.validation.constraints.MinItem;
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
public class MinItemValidator extends AbstractValidator
        implements ConstraintValidator<MinItem, Collection<?>> {

    private int value;
    private IExpression expression;
    private TypeChecker type;
    private DynamicConfiguration dynamicConfiguration;

    @Autowired
    public MinItemValidator(DynamicConfiguration dynamicConfiguration) {
        this.dynamicConfiguration = dynamicConfiguration;
    }


    @Override
    public void initialize(MinItem constraintAnnotation) {
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
        int minItem = !isExpression(expression) ? value : expression.getAsInteger();
        if (itemCount >= minItem) {
            return true;
        }
        addMessageParameter(context, "value", minItem);
        if (type != null) {
            addMessageParameter(context, "type", type.getType());
        }
        return false;
    }

}
