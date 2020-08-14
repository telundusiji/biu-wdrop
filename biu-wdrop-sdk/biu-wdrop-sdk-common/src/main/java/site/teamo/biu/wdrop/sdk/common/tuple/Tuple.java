package site.teamo.biu.wdrop.sdk.common.tuple;

/**
 * @author 爱做梦的锤子
 * @create 2020/8/14
 */
public class Tuple {

    public static <T1, T2> Tuple2<T1, T2> tuple2(T1 t1, T2 t2) {
        return Tuple2.<T1, T2>builder()
                .t1(t1)
                .t2(t2)
                .build();
    }

    public static <T1, T2, T3> Tuple3<T1, T2, T3> tuple3(T1 t1, T2 t2, T3 t3) {
        return Tuple3.<T1, T2, T3>builder()
                .t1(t1)
                .t2(t2)
                .t3(t3)
                .build();
    }

}
