package site.teamo.biu.wdrop.base.conf;

import site.teamo.biu.wdrop.base.exception.ConfigException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author 爱做梦的锤子
 * @create 2020/11/4
 */
public class Config {
    private final Map<ConfigEntry, Object> conf;

    private Config(Map<ConfigEntry, Object> conf) {
        this.conf = conf;
    }

    public <T> Optional<T> get(ConfigEntry<T> configEntry) {
        if (configEntry == null) {
            return Optional.empty();
        }
        return configEntry.value(conf);
    }

    public <T> T getOrElseThrow(ConfigEntry<T> configEntry) {
        if (configEntry == null) {
            throw new ConfigException("Nonexistent configuration item[" + configEntry.key + "]");
        }
        return configEntry.value(conf).orElseThrow(() -> new ConfigException("[" + configEntry.key + "]=>" + configEntry.doc));
    }

    public String getString(ConfigEntry configEntry) {
        if (configEntry == null) {
            return null;
        }
        return configEntry.valueString(conf);
    }

    public static Builder builer() {
        return new Builder();
    }

    public static class Builder {
        private Map<ConfigEntry, Object> conf = new HashMap<>();

        public <T> Builder set(ConfigEntry<T> configEntry, T value) {
            conf.put(configEntry, value);
            return this;
        }

        public Config build() {
            return new Config(conf);
        }
    }

}
