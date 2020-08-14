package site.teamo.biu.plugin.test;


import site.teamo.biu.wdrop.sdk.common.tuple.Tuple;
import site.teamo.biu.wdrop.sdk.starter.JavaJobContext;
import site.teamo.biu.wdrop.sdk.starter.Plugin;
import site.teamo.biu.wdrop.sdk.starter.SparkJobContext;
import site.teamo.biu.wdrop.sdk.starter.annotation.JobContext;
import site.teamo.biu.wdrop.sdk.starter.annotation.PluginMainClass;

import java.util.Arrays;

/**
 * @author 爱做梦的锤子
 * @create 2020/8/14
 */
class JavaPluginTestTest {

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {

        String a = "123";

        Class<?> c = Class.forName("site.teamo.biu.plugin.test.JavaPluginTest");

        if (!c.isAnnotationPresent(PluginMainClass.class)) {
            return;
        }

        PluginMainClass pluginMainClass = c.getAnnotation(PluginMainClass.class);
        Class<?> in = pluginMainClass.in();
        Class<?> out = pluginMainClass.out();

        Plugin plugin = (Plugin) c.newInstance();

        Arrays.stream(c.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(JobContext.class))
                .map(field -> Tuple.tuple2(field, field.getAnnotation(JobContext.class)))
                .filter(fieldJobContextTuple2 -> fieldJobContextTuple2.getT2() != null)
                .map(fieldJobContextTuple2 -> Tuple.tuple2(fieldJobContextTuple2.getT1(), fieldJobContextTuple2.getT2().value()))
                .forEach(fieldJobContextTypeTuple2 -> {
                    try {
                        fieldJobContextTypeTuple2.getT1().setAccessible(true);
                        switch (fieldJobContextTypeTuple2.getT2()) {
                            case SPARK:
                                fieldJobContextTypeTuple2.getT1().set(plugin, new SparkJobContext());
                                break;
                            case JAVA:
                                fieldJobContextTypeTuple2.getT1().set(plugin, new JavaJobContext());
                                break;
                            default:
                                break;
                        }
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }

                });

        Object handle = out.cast(plugin.handle(in.cast(a)));

        System.out.println(handle);

    }

}