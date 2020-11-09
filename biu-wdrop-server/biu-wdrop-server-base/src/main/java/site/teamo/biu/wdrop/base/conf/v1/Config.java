package site.teamo.biu.wdrop.base.conf.v1;

import site.teamo.biu.wdrop.base.exception.ConfigException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
            throw new ConfigException("Cannot use an empty ConfigEntry to get the configuration content");
        }
        return configEntry.value(conf).orElseThrow(() -> new ConfigException("Null config value [" + configEntry.key + "]=>" + configEntry.doc));
    }


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Map<ConfigEntry, Object> conf = new HashMap<>();

        public <T> Builder config(ConfigEntry<T> configEntry, T value) {
            conf.put(configEntry, value);
            return this;
        }

        public Config build() {
            return new Config(conf);
        }
    }

}
