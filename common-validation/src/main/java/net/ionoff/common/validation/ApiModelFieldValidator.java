package net.ionoff.common.validation;

import net.ionoff.common.validation.annotation.IValidator;
import net.ionoff.common.validation.DynamicConfiguration;

import java.util.ArrayList;
import java.util.List;

public final class ApiModelFieldValidator {

    private final String fieldName;
    private final List<IValidator> fieldValidators;

    public ApiModelFieldValidator(String fieldName) {
        this.fieldName = fieldName;
        this.fieldValidators = new ArrayList<>();
    }

    public String getFieldName() {
        return fieldName;
    }

    public List<IValidator> getFieldValidators() {
        return fieldValidators;
    }

    public void setDynamicConfiguration(DynamicConfiguration dynamicConfiguration) {
        for (IValidator validator : fieldValidators) {
            validator.setDynamicConfiguration(dynamicConfiguration);
        }
    }
}
