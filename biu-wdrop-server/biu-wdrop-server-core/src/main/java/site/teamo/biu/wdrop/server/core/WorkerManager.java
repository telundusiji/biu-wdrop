package site.teamo.biu.wdrop.server.core;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 爱做梦的锤子
 * @create 2020/8/14
 */
@Slf4j
public class WorkerManager {

    private static Map<String, WorkerHandler> workerHandlerMap = new ConcurrentHashMap<>();

    public static void allocatedWorker() throws Exception {
        WorkerHandler workerHandler = WorkerHandler.create();
        workerHandlerMap.put(workerHandler.getWorkerId(), workerHandler);
    }

    public static void update(String workId, String pid) {
        WorkerHandler workerHandler = workerHandlerMap.get(workId);
        workerHandler.setPid(pid);
    }

}
