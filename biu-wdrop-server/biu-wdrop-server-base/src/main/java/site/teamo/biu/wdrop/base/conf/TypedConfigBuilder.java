package site.teamo.biu.wdrop.base.conf;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author 爱做梦的锤子
 * @create 2020/11/4
 */
public class TypedConfigBuilder<T> {
    private final ConfigBuilder<T> parent;
    private final Function<String, T> converter;
    private final Function<T, String> stringConverter;

    public TypedConfigBuilder(ConfigBuilder parent, Function<String, T> converter, Function<T, String> stringConverter) {
        this.parent = parent;
        this.converter = converter;
        this.stringConverter = stringConverter;
    }

    public TypedConfigBuilder(ConfigBuilder parent, Function<String, T> converter) {
        this(parent, converter, t -> Optional.ofNullable(t).map(Object::toString).orElse(null));
    }

    public ConfigEntry<T> createWithDefaultValue(T defaultValue) {
        T transformedDefault = converter.apply(stringConverter.apply(defaultValue));
        ConfigEntryWithDefaultValue<T> entry = new ConfigEntryWithDefaultValue<>(
                parent.key,
                transformedDefault,
                converter,
                stringConverter,
                parent.doc,
                parent.isPublic,
                parent.validate);
        parent.onCreate.ifPresent(consumer -> consumer.accept(entry));
        return entry;
    }

    public ConfigEntry<T> createWithDefaultFunction(Supplier<T> defaultFunction) {
        ConfigEntryWithDefaultFunction<T> entry = new ConfigEntryWithDefaultFunction<>(
                parent.key,
                defaultFunction,
                converter,
                stringConverter,
                parent.doc,
                parent.isPublic,
                parent.validate);
        parent.onCreate.ifPresent(consumer -> consumer.accept(entry));
        return entry;
    }

    public ConfigEntry<T> createWithOptional() {
        ConfigEntryWithOptional<T> entry = new ConfigEntryWithOptional<>(
                parent.key,
                converter,
                stringConverter,
                parent.doc,
                parent.isPublic,
                parent.validate);
        parent.onCreate.ifPresent(consumer -> consumer.accept(entry));
        return entry;
    }
}
