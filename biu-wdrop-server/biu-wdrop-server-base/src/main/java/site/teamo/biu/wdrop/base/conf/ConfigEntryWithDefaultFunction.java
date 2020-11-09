package site.teamo.biu.wdrop.base.conf;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author 爱做梦的锤子
 * @create 2020/11/4
 */
public class ConfigEntryWithDefaultFunction<T> extends ConfigEntry<T> {

    private final Supplier<T> defaultFunction;

    public ConfigEntryWithDefaultFunction(String key,
                                          Supplier<T> defaultFunction,
                                          String doc,
                                          Optional<Predicate<Optional<T>>> validate) {
        super(key, doc,validate);
        this.defaultFunction = defaultFunction;
    }

    /**
     * 获取配置的默认值
     *
     * @return
     */
    public Optional<T> defaultValue() {
        return Optional.ofNullable(defaultFunction.get());
    }

}
