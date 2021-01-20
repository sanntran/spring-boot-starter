package net.ionoff.common.validation.expression;

import java.util.List;

public class ArrayValue implements IExpression {

    public static final String PREFIX = "$array::";
    private List<Object> values;

    public ArrayValue(List<Object> values) {
        this.values = values;
    }

    @Override
    public Object get() {
        return getValues();
    }

    @Override
    public boolean isStatic() {
        return true;
    }

    public List<Object> getValues() {
        return values;
    }
}
