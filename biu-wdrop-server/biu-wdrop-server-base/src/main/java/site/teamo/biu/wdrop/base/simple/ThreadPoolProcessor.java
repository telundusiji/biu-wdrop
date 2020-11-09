package site.teamo.biu.wdrop.base.simple;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import site.teamo.biu.wdrop.base.Container;
import site.teamo.biu.wdrop.base.Processor;
import site.teamo.biu.wdrop.base.conf.Config;
import site.teamo.biu.wdrop.base.exception.ShutdownProcessorException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author 爱做梦的锤子
 * @create 2020/11/4
 */
@Slf4j
public class ThreadPoolProcessor<IN, OUT> implements Processor<IN, OUT> {

    private volatile boolean running = false;

    private final Configuration<IN, OUT> configuration;

    public ThreadPoolProcessor(Config config) {

        /**
         * 检验并行度创建处理线程池
         */
        Integer parallelismTemp = config.getOrElseThrow(ThreadPoolProcessorConfig.parallelism);
        ExecutorService executor = config.get(ThreadPoolProcessorConfig.executor)
                .orElseGet(() -> Executors.newFixedThreadPool(parallelismTemp));

        /**
         * 构造配置信息对象
         */
        configuration = Configuration.<IN, OUT>builder()
                .name(config.getOrElseThrow(ThreadPoolProcessorConfig.name))
                .inContainer(config.getOrElseThrow(ThreadPoolProcessorConfig.inContainer))
                .outSuccessfulContainer(config.getOrElseThrow(ThreadPoolProcessorConfig.outSuccessfulContainer))
                .outFailedContainer(config.get(ThreadPoolProcessorConfig.outFailedContainer).orElse(Container.NONE))
                .process(config.getOrElseThrow(ThreadPoolProcessorConfig.process))
                .intervalTime(config.getOrElseThrow(ThreadPoolProcessorConfig.intervalTime))
                .executor(executor)
                .parallelism(((ThreadPoolExecutor) executor).getPoolSize())
                .build();


    }

    @Override
    public String name() {
        return configuration.getName();
    }

    @Override
    public void start() {
        if (running) {
            return;
        }

        /**
         * 如果线程池没有设置或者被关闭，则构建处理器线程池
         */
        running = true;
        log.info("Thread pool processor[{}] start...", name());
        for (int i = configuration.getParallelism(); i > 0; i--) {
            configuration.getFutures().add(configuration.getExecutor().submit(() -> {
                while (running) {
                    IN in = configuration.getInContainer().takeOut();
                    if (in == null) {
                        try {
                            Thread.sleep(configuration.getIntervalTime());
                        } catch (InterruptedException e) {
                            log.warn("The processor [{}]'s sleep was interrupted when it encountered an empty element", name());
                        }
                        continue;
                    }
                    configuration.getProcessingElements().add(in);
                    try {
                        OUT out = configuration.getProcess().apply(in);
                        configuration.getOutSuccessfulContainer().putIn(out);
                    } catch (Throwable throwable) {
                        configuration.getOutFailedContainer().putIn(in);
                        log.warn("An exception occurred while processing the element[{}]", in, throwable);
                    } finally {
                        configuration.getProcessingElements().remove(in);
                    }
                }
            }));
        }
    }

    @Override
    public void shutdown() {
        if (!running) {
            return;
        }
        running = false;
        log.info("Thread pool processor[{}] shutdown", name());
        try {
            List<Future> futures = configuration.getFutures();
            List<Throwable> throwableList = new ArrayList<>();
            for (int i = 0; i < futures.size(); i++) {
                try {
                    futures.get(i).get();
                } catch (Throwable throwable) {
                    throwableList.add(throwable);
                }
            }
            throwableList = throwableList.stream()
                    .filter(throwable -> throwable != null)
                    .collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(throwableList)) {
                ShutdownProcessorException exception = new ShutdownProcessorException();
                throwableList.forEach(throwable -> exception.addSuppressed(throwable));
                throw exception;
            }
        } finally {
            configuration.getExecutor().shutdown();
        }

    }

    @Override
    public void shutdownNow() {
        if (!running) {
            return;
        }
        running = false;
        log.info("Thread pool processor[{}] shutdown now", name());
        configuration.getExecutor().shutdownNow();
    }

    @Override
    public List<IN> processingElement() {
        return new ArrayList<>(configuration.getProcessingElements());
    }

    @Override
    public Config config() {
        return Config.builder()
                .config(ThreadPoolProcessorConfig.name, configuration.getName())
                .config(ThreadPoolProcessorConfig.parallelism, configuration.getParallelism())
                .config(ThreadPoolProcessorConfig.intervalTime, configuration.getIntervalTime())
                .build();
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Configuration<IN, OUT> {
        private String name;
        private Container<IN> inContainer;
        private Container<OUT> outSuccessfulContainer;
        private Container<IN> outFailedContainer;
        private Function<IN, OUT> process;
        private Integer parallelism;
        private ExecutorService executor;
        private Long intervalTime;
        private Boolean throwingException;
        private List<IN> processingElements = new ArrayList<>();
        private List<Future> futures = new ArrayList<>();
    }

}
