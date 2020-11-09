package site.teamo.biu.wdrop.base.conf;


import java.util.Optional;
import java.util.function.Predicate;

/**
 * @author 爱做梦的锤子
 * @create 2020/11/4
 */
public class ConfigEntryWithOptional<T> extends ConfigEntry<T> {

    public ConfigEntryWithOptional(String key,
                                   String doc,
                                   Optional<Predicate<Optional<T>>> validate) {
        super(key, doc, validate);
    }

    /**
     * 获取配置的默认值
     *
     * @return
     */
    public Optional<T> defaultValue() {
        return Optional.empty();
    }
}
