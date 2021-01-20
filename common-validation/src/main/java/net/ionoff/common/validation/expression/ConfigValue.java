package net.ionoff.common.validation.expression;

import net.ionoff.common.validation.DynamicConfiguration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ConfigValue implements IExpression {

    public static final String PREFIX = "$config::";

    private String configKey;
    private String defaultValue;
    private DynamicConfiguration dynamicConfiguration;

    public ConfigValue(String configKey, String defaultValue) {
        this.configKey = configKey;
        this.defaultValue = defaultValue;
    }
    
    @Override
    public Object get() {
        return dynamicConfiguration.getStringValue(configKey, defaultValue);
    }

    public String getConfigKey() {
        return configKey;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    @Override
    public void setDynamicConfiguration(DynamicConfiguration dynamicConfiguration) {
        this.dynamicConfiguration = dynamicConfiguration;
    }

    @Override
    public Collection<Object> getAsCollection() {
        String value = (String) get();
        String[] values = value.split(",");
        List<Object> valueList = new ArrayList<>(values.length);
        for (String val : values) {
            valueList.add(val.trim());
        }
        return valueList;
    }

}
