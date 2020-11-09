package site.teamo.biu.wdrop.base;


import site.teamo.biu.wdrop.base.conf.Config;

import java.util.List;

/**
 * @author 爱做梦的锤子
 * @create 2020/11/3
 */
public interface Processor<IN, OUT> {
    String name();

    void start();

    void shutdown();

    void shutdownNow();

    List<IN> processingElement();

    Config config();
}
