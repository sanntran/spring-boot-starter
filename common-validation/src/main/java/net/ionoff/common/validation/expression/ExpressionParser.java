package net.ionoff.common.validation.expression;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class ExpressionParser {

    private ExpressionParser() {
        // prevent creating instant
    }

    private static String getContent(String detail, String type, boolean hasCurlyBrackets) {
        String content = type.isEmpty() ? detail : detail.substring(type.length());
        if (!hasCurlyBrackets) {
            return content;
        }
        if (!content.startsWith("{") || !content.endsWith("}")) {
            throw new IllegalArgumentException(String.format("Invalid property '%s'. Cannot get content", detail));
        }
        return content.substring(1, content.length() - 1).trim();
    }

    public static IExpression parse(String propertyValueString) {
        if (propertyValueString.startsWith(ConfigValue.PREFIX)) {
            return parseConfigValue(propertyValueString);

        } else if (propertyValueString.startsWith(ReflectionValue.PREFIX)) {
            return parseReflectionValue(propertyValueString);

        } else if (propertyValueString.startsWith(ArrayValue.PREFIX)
                || (propertyValueString.startsWith("{") && propertyValueString.endsWith("}"))) {
            return parseArrayValue(propertyValueString);
        } else {
            return new StringValue(propertyValueString);
        }
    }

    private static ReflectionValue parseReflectionValue(String propertyValueString) {
        String content = getContent(propertyValueString, ReflectionValue.PREFIX, false);
        String[] classAndMethodArr = content.split("#");
        try {
            Class<?> clazz = Class.forName(classAndMethodArr[0]);
            Method method = clazz.getMethod(classAndMethodArr[1]);
            return new ReflectionValue(clazz, method);
        } catch (NullPointerException | ClassNotFoundException | NoSuchMethodException e) {
            throw new IllegalArgumentException(String.format("Cannot parse reflection method from '%s'. '%s'", content, e.getMessage()), e);
        }
    }

    private static ArrayValue parseArrayValue(String propertyValueString) {
        String type = propertyValueString.startsWith(ArrayValue.PREFIX) ? ArrayValue.PREFIX : "";
        String content = getContent(propertyValueString, type, true);
        List<Object> values = Arrays.stream(content.split(",")).map(String::trim).collect(Collectors.toList());
        return new ArrayValue(values);
    }

    private static ConfigValue parseConfigValue(String propertyValueString) {
        String content = getContent(propertyValueString, ConfigValue.PREFIX, true);
        int firstCommaIndex = content.indexOf(',');
        if (firstCommaIndex < 0) {
            throw new IllegalArgumentException("Content must contains key and default value, use ',' as separator");
        }
        String configKey = content.substring(0, firstCommaIndex).trim();
        String defaultValue = content.substring(firstCommaIndex + 1).trim();
        return new ConfigValue(configKey, defaultValue);
    }

}
