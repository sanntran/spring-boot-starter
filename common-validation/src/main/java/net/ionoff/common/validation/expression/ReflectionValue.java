package net.ionoff.common.validation.expression;

import net.ionoff.common.validation.exception.ReflectionInvokeException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionValue implements IExpression {

    public static final String PREFIX = "$reflection::";

    private Class<?> clazz;
    private Method method;

    public ReflectionValue(Class<?> clazz, Method method) {
        this.clazz = clazz;
        this.method = method;
    }

    @Override
    public Object get() {
        try {
            return method.invoke(null);
        } catch (InvocationTargetException e) {
            throw new ReflectionInvokeException(String.format("Error when invoke method '%s' of class '%s'. %s",
                    method.getName(), clazz.getCanonicalName(), e.getTargetException().getMessage()), e);
        } catch (Exception e) {
            throw new ReflectionInvokeException(String.format("Error when invoke method '%s' of class %s. %s",
                    method.getName(), clazz.getCanonicalName(), e.getMessage()), e);
        }
    }

    public Method getMethod() {
        return method;
    }
}
