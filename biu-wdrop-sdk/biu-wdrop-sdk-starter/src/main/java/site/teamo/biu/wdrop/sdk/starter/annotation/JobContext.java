package site.teamo.biu.wdrop.sdk.starter.annotation;

import com.google.inject.internal.cglib.core.$ClassEmitter;

import java.lang.annotation.*;

/**
 * @author 爱做梦的锤子
 * @create 2020/8/13
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Documented
public @interface JobContext {

    JobContextType value();

    enum JobContextType {
        JAVA,
        SPARK;
    }
}
