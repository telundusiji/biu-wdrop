package site.teamo.biu.wdrop.base.conf;

import lombok.Data;
import site.teamo.biu.wdrop.base.exception.ConfigException;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author 爱做梦的锤子
 * @create 2020/11/4
 */
@Data
public abstract class ConfigEntry<T> {

    public static final String UNDEFINED = "<undefine>";

    protected final String key;
    protected final Function<String, T> valueConverter;
    protected final Function<T, String> stringConverter;
    protected final String doc;
    protected final boolean isPublic;
    protected Optional<Predicate<Optional<T>>> validate = Optional.empty();

    public ConfigEntry(String key,
                       Function<String, T> valueConverter,
                       Function<T, String> stringConverter,
                       String doc,
                       boolean isPublic,
                       Optional<Predicate<Optional<T>>> validate) {
        this.key = key;
        this.valueConverter = valueConverter;
        this.stringConverter = stringConverter;
        this.doc = doc;
        this.isPublic = isPublic;
        this.validate = validate;
    }

    public abstract Optional<T> defaultValue();

    public abstract String defaultValueString();

    public Optional<T> value(Map<ConfigEntry, Object> conf) {
        Optional<T> value = Optional.ofNullable(conf.get(this))
                .map(o -> Optional.ofNullable((T) o))
                .filter(o -> o.isPresent())
                .orElseGet(() -> defaultValue());
        if (validate.isPresent()) {
            validate.map(predicate -> predicate.test(value))
                    .filter(Boolean::booleanValue)
                    .orElseThrow(() -> new ConfigException("[" + key + " : " + value.orElse( null) + "]=>" + doc));
        }
        return value;

    }

    public String valueString(Map<ConfigEntry, Object> conf) {
        return value(conf)
                .map(t -> stringConverter.apply(t))
                .orElseGet(() -> defaultValueString());
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

    @Override
    public String toString() {
        return "ConfigEntry{" +
                "key='" + key + '\'' +
                ", valueConverter=" + valueConverter +
                ", stringConverter=" + stringConverter +
                ", doc='" + doc + '\'' +
                ", isPublic=" + isPublic +
                ", validate=" + validate +
                '}';
    }
}
