package site.teamo.biu.wdrop.sdk.starter;

import java.io.Serializable;

/**
 * @author 爱做梦的锤子
 * @create 2020/8/14
 */
public interface Plugin<In, Out> extends Serializable {

    Out handle(In in);

}
