package net.ionoff.common.validation.validator;

import net.ionoff.common.validation.constraints.TypeChecker;
import net.ionoff.common.validation.exception.GetFieldValueException;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public final class ValidatorSupport {

    private ValidatorSupport() {
        // no instance
    }

    static int countItem(Collection<?> collection, TypeChecker typeChecker) {
        if (collection == null) {
            return 0;
        }
        if (typeChecker == null) {
            return collection.size();
        }
        int count = 0;
        for (Object obj : collection) {
            if (typeChecker.checkType(obj)) {
                count++;
            }
        }
        return count;
    }

    public static Collection<Object> getAsCollection(Object object) {
        if (object == null) {
            return Collections.emptyList();
        }
        if (object instanceof Object[]) {
            return Arrays.asList((Object[]) object);
        }
        if (object instanceof Collection) {
            return (Collection) object;
        }
        return Arrays.asList(object);
    }

    public static Collection<Optional<BigDecimal>> getAsBigDecimalCollection(Object object) {
        Collection<?> collection = getAsCollection(object);
        return collection.stream().map(ValidatorSupport::getAsBigDecimal).collect(Collectors.toList());
    }

    public static Optional<BigDecimal> getAsBigDecimal(Object fieldValue) {
        if (fieldValue == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(new BigDecimal(String.valueOf(fieldValue)));
        } catch (Exception e) {
            // cannot parse to BigDecimal
            return Optional.empty();
        }
    }

    public static Object getFieldValue(String fieldName, Object object) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException | ClassCastException e) {
            throw new GetFieldValueException(String.format("Error when getting value of field '%s' from model '%s'",
                    fieldName, object.getClass().getCanonicalName()));
        }
    }

    public static boolean isValidUUID(Object value) {
        if (value == null || value instanceof UUID) {
            return true;
        }
        try {
            UUID.fromString(String.valueOf(value));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean isValidNotNullUUID(Object value) {
        if (value == null) {
            return false;
        }
        if (value instanceof UUID) {
            return true;
        }
        try {
            UUID.fromString(String.valueOf(value));
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
