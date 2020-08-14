package site.teamo.biu.wdrop.server.core;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 爱做梦的锤子
 * @create 2020/8/14
 */
@Slf4j
public class WorkerManager {

    private static final int CAPACITY = 10;

    private static Map<String, WorkerHandler> workerHandlerMap = new ConcurrentHashMap<>();

    private static ExecutorService executorService = Executors.newFixedThreadPool(1);

    static {
        executorService.submit(checkWorkerHandler());
    }

    public static void allocatedWorker() throws Exception {
        if(workerHandlerMap.size()>10){
            clean();
        }
        WorkerHandler workerHandler = WorkerHandler.create();
        workerHandlerMap.put(workerHandler.getWorkerId(), workerHandler);
    }

    public static void update(String workId, String pid) {
        WorkerHandler workerHandler = workerHandlerMap.get(workId);
        workerHandler.setPid(pid);
    }

    private static void clean() {
        Iterator<Map.Entry<String, WorkerHandler>> iterator = workerHandlerMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, WorkerHandler> entry = iterator.next();
            if (entry.getValue().isDestroyed()) {
                iterator.remove();
            }
        }
    }

    private static Callable checkWorkerHandler() {
        return () -> {
            while (true) {
                workerHandlerMap.entrySet().forEach(entry -> {
                    try {
                        entry.getValue().destroy();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                Thread.sleep(1000);
            }
        };
    }

}
