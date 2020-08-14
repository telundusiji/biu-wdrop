package site.teamo.biu.wdrop.server.core;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.*;
import java.util.UUID;

/**
 * @author 爱做梦的锤子
 * @create 2020/8/14
 */

public class WorkerHandler {

    private static final String WORKER_APPLICATION_CLASSNAME = "site.teamo.biu.wdrop.worker.WorkApplication";

    private Process process;

    private String workerId;

    private String pid;

    private WorkerHandler(String workerId, Process process) {
        this.workerId = workerId;
        this.process = process;
    }

    public static WorkerHandler create() throws IOException {
        String workerId = UUID.randomUUID().toString();
        String serverName = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getServerName();
        Integer port = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getServerPort();
        Runtime run = Runtime.getRuntime();
        String java = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        String cp = "\"" + System.getProperty("java.class.path");
        cp += File.pathSeparator + ClassLoader.getSystemResource("").getPath() + "\"";
        String cmd = java + " -cp " + cp + " " + WORKER_APPLICATION_CLASSNAME + " " + workerId + " " + serverName + ":" + port;
        Process process = run.exec(cmd);
        return new WorkerHandler(workerId,process);
    }


    public Process getProcess() {
        return process;
    }

    public String getWorkerId() {
        return workerId;
    }

    public String getPid() {
        return pid;
    }

    public WorkerHandler setPid(String pid) {
        this.pid = pid;
        return this;
    }
}
