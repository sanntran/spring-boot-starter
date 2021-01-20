package net.ionoff.common.validation.constraints;

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
