package site.teamo.biu.wdrop.base.simple;

import site.teamo.biu.wdrop.base.Container;
import site.teamo.biu.wdrop.base.Processor;
import site.teamo.biu.wdrop.base.conf.v1.Config;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

/**
 * @author 爱做梦的锤子
 * @create 2020/11/4
 */
public class ThreadPoolProcessor<IN, OUT> implements Processor<IN, OUT> {

    private volatile boolean running = false;

    private Config config;

    private final String name;
    private final Container<IN> inContainer;
    private final Container<OUT> outContainer;
    private final Function<IN, OUT> process;
    private final Integer parallelism;
    private ExecutorService executor;

    public ThreadPoolProcessor(String name, Container<IN> inContainer, Container<OUT> outContainer, Function<IN, OUT> process, int parallelism) {
        this.name = name;
        this.inContainer = inContainer;
        this.outContainer = outContainer;
        this.process = process;
        this.parallelism = parallelism;
    }

    public ThreadPoolProcessor(Config config) {
        this.parallelism = config.getOrElseThrow(ThreadPoolProcessorConfig.parallelism);
        this.name = config.getOrElseThrow(ThreadPoolProcessorConfig.name);
        this.inContainer = config.getOrElseThrow(ThreadPoolProcessorConfig.inContainer);
        this.outContainer = config.getOrElseThrow(ThreadPoolProcessorConfig.outContainer);
        this.process = config.getOrElseThrow(ThreadPoolProcessorConfig.process);

        System.out.println("");
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public void start() {
        if (running) {
            return;
        }
        try {
            /**
             * 如果线程池没有设置或者被关闭，则构建处理器线程池
             */
            if (executor == null || executor.isShutdown()) {
                executor = Executors.newFixedThreadPool(parallelism);
            }
            for (int i = parallelism; i > 0; i--) {
                executor.submit(() -> {
                    IN in = inContainer.takeOut();
                    if (in == null) {

                    }
                });
            }
        } finally {
            running = true;
        }
    }

    @Override
    public void shutdown() {
        if (!running) {
            return;
        }
    }

    @Override
    public void shutdownNow() {
        if (!running) {
            return;
        }
    }

    @Override
    public List<IN> processingElement() {
        return null;
    }

    @Override
    public Config config() {
        return null;
    }

}
