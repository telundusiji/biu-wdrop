package site.teamo.biu.wdrop.base.simple;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import site.teamo.biu.wdrop.base.Container;
import site.teamo.biu.wdrop.base.conf.ConfigEntry;
import site.teamo.biu.wdrop.base.conf.ConfigEntryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author 爱做梦的锤子
 * @create 2020/11/4
 */
@Slf4j
public class ThreadPoolProcessorConfig {


    private ThreadPoolProcessorConfig() {
    }

    private static Consumer<ConfigEntry> register = (entry) -> log.debug("[Thread Pool Processor default config] => {}", entry.toString());

    public static final ConfigEntry<String> name = new ConfigEntryBuilder<String>("name")
            .onCreate(configEntry -> register.accept(configEntry))
            .doc("Processor name,this config cannot by empty")
            .validate(ov -> ov.map(v -> StringUtils.isNotBlank(v)).orElse(false))
            .createWithDefaultFunction(() -> "thread-pool-processor-" + System.currentTimeMillis());

    public static final ConfigEntry<Integer> parallelism = new ConfigEntryBuilder<Integer>("parallelism")
            .onCreate(configEntry -> register.accept(configEntry))
            .validate(ov -> ov.map(v -> v > 0).orElse(false))
            .doc("The parallelism of the processor should not be less than 0 or empty. It is recommended that the parallelism should not exceed 2 times of the number of CPU cores")
            .createWithDefaultValue(1);

    public static final ConfigEntry<Container> inContainer = new ConfigEntryBuilder<Container>("inContainer")
            .onCreate(configEntry -> register.accept(configEntry))
            .validate(ov -> ov.map(v -> v != null).orElse(false))
            .doc("Input source for processing data by processor. The configuration cannot be empty")
            .createWithOptional();

    public static final ConfigEntry<Container> outSuccessfulContainer = new ConfigEntryBuilder<Container>("outSuccessfulContainer")
            .onCreate(configEntry -> register.accept(configEntry))
            .validate(ov -> ov.map(v -> v != null).orElse(false))
            .doc("The processor processes the output source of data. The configuration cannot be empty")
            .createWithOptional();

    public static final ConfigEntry<Container> outFailedContainer = new ConfigEntryBuilder<Container>("outFailedContainer")
            .onCreate(configEntry -> register.accept(configEntry))
            .validate(ov -> ov.map(v -> v != null).orElse(false))
            .doc("The output source of the failed data processed by the processor")
            .createWithDefaultValue(Container.NONE);

    public static final ConfigEntry<ExecutorService> executor = new ConfigEntryBuilder<ExecutorService>("executor")
            .onCreate(configEntry -> register.accept(configEntry))
            .doc("The thread pool used by the processor to run. When the configuration is empty, the thread pool is created according to the parallelism parameter. When the configuration is not empty, the thread pool specified by the configuration is used")
            .createWithOptional();

    public static final ConfigEntry<Function> process = new ConfigEntryBuilder<Function>("process")
            .onCreate(configEntry -> register.accept(configEntry))
            .validate(ov -> ov.map(v -> v != null).orElse(false))
            .doc("The logical method processed by the processor. The configuration cannot be empty")
            .createWithOptional();

    public static final ConfigEntry<Long> intervalTime = new ConfigEntryBuilder<Long>("intervalTime")
            .onCreate(configEntry -> register.accept(configEntry))
            .validate(ov -> ov.map(v -> v >= 0L).orElse(false))
            .doc("When the processor encounters an empty element, the interval to continue processing should be greater than or equal to 0ms. The default value is 200ms")
            .createWithDefaultValue(200L);
}
