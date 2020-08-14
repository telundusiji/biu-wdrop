package site.teamo.biu.wdrop.sdk.common.tuple;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 爱做梦的锤子
 * @create 2020/8/14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tuple3<T1,T2,T3> {

    private T1 t1;
    private T2 t2;
    private T3 t3;

}
