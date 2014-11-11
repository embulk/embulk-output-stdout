package org.quickload.config;

import java.lang.reflect.Method;
import com.google.common.base.Optional;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ConfigSource
        extends DataSource<ConfigSource>
{
    private static final FieldMapper fieldMapper = new FieldMapper() {
        @Override
        public Optional<String> getJsonKey(Method getterMethod)
        {
            return configJsonKey(getterMethod);
        }

        @Override
        public Optional<String> getDefaultJsonString(Method getterMethod)
        {
            return configDefaultJsonValue(getterMethod);
        }
    };

    public ConfigSource()
    {
        super(fieldMapper);
    }

    ConfigSource(ObjectNode data)
    {
        super(data, fieldMapper);
    }

    private static Optional<String> configJsonKey(Method getterMethod)
    {
        Config a = getterMethod.getAnnotation(Config.class);
        if (a != null) {
            return Optional.of(a.value());
        } else {
            return Optional.absent();
        }
    }

    private static Optional<String> configDefaultJsonValue(Method getterMethod)
    {
        ConfigDefault a = getterMethod.getAnnotation(ConfigDefault.class);
        if (a != null && !a.value().isEmpty()) {
            return Optional.of(a.value());
        } else {
            return Optional.absent();
        }
    }

    public ConfigSource deepCopy()
    {
        return new ConfigSource(data.deepCopy());
    }
}
