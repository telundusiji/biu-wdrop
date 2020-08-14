package site.teamo.biu.wdrop.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 爱做梦的锤子
 * @create 2020/8/14
 */
@SpringBootApplication(excludeName = "site.teamo.biu.wdrop.server.worker.WorkApplication")
public class WDropApplication {
    public static void main(String[] args) {
        SpringApplication.run(WDropApplication.class, args);
    }
}
