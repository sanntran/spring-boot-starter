package net.ionoff.common.validation;

import net.ionoff.common.validation.exception.GetFieldValueException;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ApiModelWrapper {

    private final Map<String, Object> fieldValueMap;

    public ApiModelWrapper(Object apiModel) {
        fieldValueMap = new HashMap<>();
        for (Field field : apiModel.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                Object value = field.get(apiModel);
                fieldValueMap.put(field.getName(), value);
            } catch (Exception e) {
                throw new GetFieldValueException(String.format("Error when get value of field '%s' from class '%s'",
                        field.getName(), apiModel.getClass().getSimpleName()), e);
            }
        }
    }

    public Set<String> getApiModelFields() {
        return fieldValueMap.keySet();
    }

    public Object getApiModelFieldValue(String fieldName) {
        return fieldValueMap.get(fieldName);
    }
}
