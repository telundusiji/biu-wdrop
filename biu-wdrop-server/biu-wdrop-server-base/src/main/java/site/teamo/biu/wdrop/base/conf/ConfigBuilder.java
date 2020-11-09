package site.teamo.biu.wdrop.base.conf;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author 爱做梦的锤子
 * @create 2020/11/4
 */
public class ConfigBuilder<T> {
    public final String key;

    protected String doc;

    protected boolean isPublic;

    protected Optional<Consumer<ConfigEntry>> onCreate = Optional.empty();

    protected Optional<Predicate<Optional<T>>> validate = Optional.empty();

    public ConfigBuilder(String key) {
        this.key = key;
    }

    public ConfigBuilder<T> onCreate(Consumer<ConfigEntry> onCreate) {
        this.onCreate = Optional.ofNullable(onCreate);
        return this;
    }

    public ConfigBuilder<T> doc(String doc) {
        this.doc = doc;
        return this;
    }

    public ConfigBuilder<T> isPublic(boolean isPublic) {
        this.isPublic = isPublic;
        return this;
    }

    public ConfigBuilder<T> validate(Predicate<Optional<T>> validate) {
        this.validate = Optional.ofNullable(validate);
        return this;
    }

    public TypedConfigBuilder<Integer> integerConf() {
        return new TypedConfigBuilder<>(this, s -> Integer.valueOf(s));
    }

    public TypedConfigBuilder<Long> longConf() {
        return new TypedConfigBuilder<>(this, s -> Long.valueOf(s));
    }

    public TypedConfigBuilder<String> stringConf() {
        return new TypedConfigBuilder<>(this, s -> String.valueOf(s));
    }

    public TypedConfigBuilder<T> conf() {
        return new TypedConfigBuilder<>(this, s -> (T) s);
    }

}
