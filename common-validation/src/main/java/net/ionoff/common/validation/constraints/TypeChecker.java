package net.ionoff.common.validation.constraints;

public interface TypeChecker {

    String getType();

    boolean checkType(Object object);

    static TypeChecker fromString(String propertyValueString) {
        try {
            Class<?> clazz = Class.forName(propertyValueString);
            return (TypeChecker) clazz.getConstructors()[0].newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format("Error when parse TypeChecker from %s. %s", propertyValueString, e.getMessage()), e);
        }
    }
}
