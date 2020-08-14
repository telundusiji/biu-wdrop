package site.teamo.biu.wdrop.server.core;

import org.springframework.util.StreamUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.*;
import java.nio.charset.Charset;
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

    private boolean destroyed;

    private WorkerHandler(String workerId, Process process) {
        this.workerId = workerId;
        this.process = process;
        this.destroyed = Boolean.FALSE;
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
        return new WorkerHandler(workerId, process);
    }

    public void destroy() throws IOException {
        if (process.isAlive() || destroyed == Boolean.TRUE) {
            return;
        }
        System.out.println(StreamUtils.copyToString(process.getInputStream(), Charset.forName("UTF-8")));
        System.out.println(StreamUtils.copyToString(process.getErrorStream(), Charset.forName("UTF-8")));
        process.destroy();
        destroyed = Boolean.TRUE;
    }

    public void destroyForce() throws IOException {
        if(destroyed==Boolean.TRUE){
            return;
        }
        if (process.isAlive()) {
            process.destroy();
        }
        System.out.println(StreamUtils.copyToString(process.getInputStream(), Charset.forName("UTF-8")));
        System.out.println(StreamUtils.copyToString(process.getErrorStream(), Charset.forName("UTF-8")));
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

    public boolean isDestroyed() {
        return destroyed;
    }
}
