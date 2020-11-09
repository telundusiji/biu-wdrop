package site.teamo.biu.wdrop.base.conf.v1;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author 爱做梦的锤子
 * @create 2020/11/4
 */
public class ConfigEntryBuilder<T> {
    public final String key;

    protected String doc;

    protected Optional<Consumer<ConfigEntry<T>>> onCreate = Optional.empty();

    protected Optional<Predicate<Optional<T>>> validate = Optional.empty();

    public ConfigEntryBuilder(String key) {
        this.key = key;
    }

    public ConfigEntryBuilder<T> onCreate(Consumer<ConfigEntry<T>> onCreate) {
        this.onCreate = Optional.ofNullable(onCreate);
        return this;
    }

    public ConfigEntryBuilder<T> doc(String doc) {
        this.doc = doc;
        return this;
    }

    public ConfigEntryBuilder<T> validate(Predicate<Optional<T>> validate) {
        this.validate = Optional.ofNullable(validate);
        return this;
    }

    public ConfigEntry<T> createWithDefaultValue(T defaultValue) {
        ConfigEntryWithDefaultValue<T> entry = new ConfigEntryWithDefaultValue<T>(
                key,
                defaultValue,
                doc,
                validate);
        onCreate.ifPresent(consumer -> consumer.accept(entry));
        return entry;
    }

    public ConfigEntry<T> createWithDefaultFunction(Supplier<T> defaultFunction) {
        ConfigEntryWithDefaultFunction<T> entry = new ConfigEntryWithDefaultFunction<>(
                key,
                defaultFunction,
                doc,
                validate);
        onCreate.ifPresent(consumer -> consumer.accept(entry));
        return entry;
    }

    public ConfigEntry<T> createWithOptional() {
        ConfigEntryWithOptional<T> entry = new ConfigEntryWithOptional<>(
                key,
                doc,
                validate);
        onCreate.ifPresent(consumer -> consumer.accept(entry));
        return entry;
    }

}
