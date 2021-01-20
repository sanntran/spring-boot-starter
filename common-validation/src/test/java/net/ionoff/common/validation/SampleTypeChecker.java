package net.ionoff.common.validation;

import net.ionoff.common.validation.annotation.property.TypeChecker;

public class SampleTypeChecker implements TypeChecker {

    @Override
    public String getType() {
        return "sample";
    }

    @Override
    public boolean checkType(Object object) {
        return "Sample".equals(object);
    }
}
