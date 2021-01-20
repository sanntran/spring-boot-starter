package net.ionoff.common.validation;

public interface DynamicConfiguration {
    String getStringValue(String configKey, String defaultValue);
}
