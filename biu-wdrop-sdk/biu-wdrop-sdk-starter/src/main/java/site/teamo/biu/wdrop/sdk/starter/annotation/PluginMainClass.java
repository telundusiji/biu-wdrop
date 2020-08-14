package site.teamo.biu.wdrop.sdk.starter.annotation;

import java.lang.annotation.*;

/**
 * @author 爱做梦的锤子
 * @create 2020/8/13
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.TYPE)
public @interface PluginMainClass {

    Class<?> in();

    Class<?> out();

}
