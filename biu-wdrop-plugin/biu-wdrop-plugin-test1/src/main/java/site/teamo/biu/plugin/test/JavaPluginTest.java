package site.teamo.biu.plugin.test;

import site.teamo.biu.wdrop.sdk.starter.JavaJobContext;
import site.teamo.biu.wdrop.sdk.starter.JavaPlugin;
import site.teamo.biu.wdrop.sdk.starter.Plugin;
import site.teamo.biu.wdrop.sdk.starter.SparkJobContext;
import site.teamo.biu.wdrop.sdk.starter.annotation.JobContext;
import site.teamo.biu.wdrop.sdk.starter.annotation.PluginMainClass;

/**
 * @author 爱做梦的锤子
 * @create 2020/8/14
 */
@PluginMainClass(in = String.class, out = String.class)
public class JavaPluginTest implements Plugin<String, String> {

    @JobContext(JobContext.JobContextType.JAVA)
    private JavaJobContext javaJobContext;

    @JobContext(JobContext.JobContextType.SPARK)
    private SparkJobContext sparkJobContext;

    @Override
    public String handle(String s) {
        System.out.println("参数" + s);
        System.out.println(javaJobContext.test());
        System.out.println(sparkJobContext.test());
        return "结果:" + s;

    }
}
