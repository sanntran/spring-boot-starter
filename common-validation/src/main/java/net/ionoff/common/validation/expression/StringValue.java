package net.ionoff.common.validation.expression;

public class StringValue implements IExpression {

    private String value;

    public StringValue(String value) {
        this.value = value;
    }

    @Override
    public Object get() {
        return getValue();
    }

    @Override
    public boolean isStatic() {
        return true;
    }

    public String getValue() {
        return value;
    }
}
