package net.ionoff.common.validation.expression;

import net.ionoff.common.validation.exception.ValueParserException;
import net.ionoff.common.validation.DynamicConfiguration;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public interface IExpression {

    /**
     * Return value object
     * @return A not null value
     */
    Object get();

    default Collection<Object> getAsCollection() {
        Object value = get();
        if (value instanceof Collection) {
            return (Collection) value;
        }
        return Arrays.asList(value);
    }

    default OffsetDateTime getAsOffsetDateTime() {
        Object value = get();
        if (value instanceof OffsetDateTime) {
            return (OffsetDateTime) value;
        } else if (value instanceof String) {
            return OffsetDateTime.parse((String)value);
        }
        throw new ValueParserException(String.format("Cannot parse '%s' to OffsetDateTime", value));
    }

    default String getAsString() {
        return get().toString();
    }

    default BigDecimal getAsBigDecimal() {
        String value = getAsString();
        try {
            return new BigDecimal(value);
        } catch (Exception e) {
            throw new ValueParserException(String.format("Cannot parse '%s' to BigDecimal", value), e);
        }
    }

    default Set<BigDecimal> getAsBigDecimalSet() {
        Collection<?> collection = getAsCollection();
        try {
            return collection.stream().map(n -> new BigDecimal(n.toString())).collect(Collectors.toSet());
        } catch (Exception e) {
            throw new ValueParserException(String.format("Cannot parse '%s' to Set of BigDecimal", get()), e);
        }
    }

    default Set<String> getAsStringSet() {
        Collection<?> collection = getAsCollection();
        try {
            return collection.stream().map(Object::toString).collect(Collectors.toSet());
        } catch (Exception e) {
            throw new ValueParserException(String.format("Cannot parse '%s' to Set of String", get()), e);
        }
    }

    default int getAsInteger() {
        String value = getAsString();
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            throw new ValueParserException(String.format("Cannot parse '%s' to Integer", value), e);
        }
    }

    default void setDynamicConfiguration(DynamicConfiguration dynamicConfiguration) {
        // child class override if needs
    }

    default boolean isStatic() {
        // default is dynamic value
        return false;
    }
}
