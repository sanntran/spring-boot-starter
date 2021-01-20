package net.ionoff.common.validation.validator;

import net.ionoff.common.validation.constraints.AllNullOrAllNotNull;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class AllNullOrAllNotNullValidator extends AbstractValidator
                        implements ConstraintValidator<AllNullOrAllNotNull, Object> {

    private List<String> fields;

    @Override
    public void initialize(AllNullOrAllNotNull constraintAnnotation) {
        fields = Arrays.asList(constraintAnnotation.fields().split(","));
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
        if (nullValueCount == 0 || nullValueCount == fields.size()) {
            return true;
        }
        addFieldsVariable(context, fields);
        return false;
    }

}
