package site.teamo.biu.wdrop.base.conf;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * @author 爱做梦的锤子
 * @create 2020/11/4
 */
public class ConfigEntryWithDefaultValue<T> extends ConfigEntry<T> {

    private final T defaultValue;

    public ConfigEntryWithDefaultValue(String key,
                                       T defaultValue,
                                       String doc,
                                       Optional<Predicate<Optional<T>>> validate) {
        super(key, doc, validate);
        this.defaultValue = defaultValue;
    }

    /**
     * 获取配置的默认值
     *
     * @return
     */
    public Optional<T> defaultValue() {
        return Optional.ofNullable(defaultValue);
    }
}
