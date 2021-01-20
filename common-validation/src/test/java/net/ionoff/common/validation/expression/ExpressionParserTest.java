package net.ionoff.common.validation.expression;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ExpressionParserTest {

    @Test
    public void parseIExpression_canParseToConfigValue() {
        IExpression IExpression = ExpressionParser.parse("$config::{config.key, default}");

        assertThat(IExpression.getClass(), equalTo(ConfigValue.class));
        ConfigValue configValue = (ConfigValue) IExpression;
        assertThat(configValue.getConfigKey(), equalTo("config.key"));
        assertThat(configValue.getDefaultValue(), equalTo("default"));
    }

    @Test
    public void parseIExpression_canParseToReflectionValue() {
        IExpression IExpression = ExpressionParser.parse("$reflection::net.ionoff.common.validation.constant.Reflections#acceptedStrings");

        assertThat(IExpression.getClass(), equalTo(ReflectionValue.class));
        ReflectionValue reflectionValue = (ReflectionValue) IExpression;
        assertThat(reflectionValue.getMethod().getName(), equalTo("acceptedStrings"));
    }

    @Test
    public void parseIExpression_canParseToArrayValue() {
        IExpression IExpression = ExpressionParser.parse("$array::{1,2,3}");

        assertThat(IExpression.getClass(), equalTo(ArrayValue.class));
        ArrayValue arrayValue = (ArrayValue) IExpression;
        assertThat(arrayValue.getValues(), equalTo(Arrays.asList("1", "2", "3")));
    }

    @Test
    public void parseIExpression_canParseToStringValue() {
        IExpression IExpression = ExpressionParser.parse("some string");

        assertThat(IExpression.getClass(), equalTo(StringValue.class));
        StringValue stringValue = (StringValue) IExpression;
        assertThat(stringValue.getValue(), equalTo("some string"));
    }
}
