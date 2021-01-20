package net.ionoff.common.validation.validator;

import net.ionoff.common.validation.constraints.AllNotNull;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class AllNotNullValidator extends AbstractValidator
                        implements ConstraintValidator<AllNotNull, Object> {

    private List<String> fields;
    private String dependOn;
    private String condition;

    @Override
    public void initialize(AllNotNull constraintAnnotation) {
        String fieldNames = constraintAnnotation.fields();
        fields = Arrays.asList(fieldNames.split(","));
        dependOn = constraintAnnotation.dependOn();
        condition = constraintAnnotation.condition();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        if (object == null) {
            return true;
        }
        int nullValueCount = 0;
        for (String fieldName : fields) {
            if (ValidatorSupport.getFieldValue(fieldName, object) == null) {
                nullValueCount++;
            }
        }
        if (nullValueCount == 0 || !checkCondition(object, dependOn, condition)) {
            return true;
        }
        addFieldsVariable(context, fields);
        return false;
    }

    private boolean checkCondition(Object object, String dependOn, String condition) {
        if ("".equals(dependOn) && "".equals(condition)) {
            // no condition
            return true;
        }
        Object dependOnValue = ValidatorSupport.getFieldValue(dependOn, object);
        if ("isNotNull".equals(condition)) {
            return dependOnValue != null;
        } else if ("isNull".equals(condition)) {
            return dependOnValue == null;
        }
        throw new IllegalArgumentException("Condition '" + condition + "' is not supported");
    }

}
