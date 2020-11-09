package site.teamo.biu.wdrop.base.conf;

import lombok.Data;
import site.teamo.biu.wdrop.base.exception.ConfigException;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * @author 爱做梦的锤子
 * @create 2020/11/4
 */
@Data
public abstract class ConfigEntry<T> {

    public static final String UNDEFINED = "<undefine>";

    protected final String key;
    protected final String doc;
    protected Optional<Predicate<Optional<T>>> validate = Optional.empty();

    public ConfigEntry(String key,
                       String doc,
                       Optional<Predicate<Optional<T>>> validate) {
        this.key = key;
        this.doc = doc;
        this.validate = validate;
    }

    public abstract Optional<T> defaultValue();

    public Optional<T> value(Map<ConfigEntry, Object> conf) {
        Optional<T> value = Optional.ofNullable(conf.get(this))
                .map(o -> Optional.ofNullable((T) o))
                .filter(o -> o.isPresent())
                .orElseGet(() -> defaultValue());
        if (validate.isPresent()) {
            validate.map(predicate -> predicate.test(value))
                    .filter(Boolean::booleanValue)
                    .orElseThrow(() -> new ConfigException("Failed verification [" + key + " : " + value.orElse( null) + "]=>" + doc));
        }
        return value;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfigEntry<?> that = (ConfigEntry<?>) o;
        return Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }
}
