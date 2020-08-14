package site.teamo.biu.wdrop.server.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import site.teamo.biu.wdrop.sdk.common.util.BiuJSONResult;
import site.teamo.biu.wdrop.server.core.WorkerManager;

/**
 * @author 爱做梦的锤子
 * @create 2020/8/14
 */
@RestController
@RequestMapping("/worker")
@Slf4j
public class WorkerController {

    @GetMapping("/start")
    public BiuJSONResult start() {
        try {
            WorkerManager.allocatedWorker();
            return BiuJSONResult.ok();
        } catch (Exception e) {
            log.error("启动出错", e);
            return BiuJSONResult.errorMsg(e.getMessage());
        }
    }

    @GetMapping("/update/{workerId}/{pid}")
    public BiuJSONResult pid(@PathVariable String workerId, @PathVariable String pid) {
        log.info("{}:{}", workerId, pid);
        WorkerManager.update(workerId, pid);
        return BiuJSONResult.ok();
    }

}
