package site.teamo.biu.wdrop.worker;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import site.teamo.biu.wdrop.sdk.common.util.BiuHttpClient;

import java.lang.management.ManagementFactory;

/**
 * @author 爱做梦的锤子
 * @create 2020/8/14
 */
@SpringBootApplication
@Slf4j
public class WorkApplication implements ApplicationRunner {

    private static String wDropServer;
    private static String workerId;

    public static void main(String[] args) {
        if (args == null || args.length != 2) {
            System.exit(-1);
        }
        workerId = args[0];
        wDropServer = args[1];
        new SpringApplicationBuilder(WorkApplication.class)
                .web(WebApplicationType.NONE)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        String pid = name.split("@")[0];
        log.info(BiuHttpClient.getClient().get("http://"+wDropServer+"/worker/update/"+workerId+"/"+pid));
    }
}
