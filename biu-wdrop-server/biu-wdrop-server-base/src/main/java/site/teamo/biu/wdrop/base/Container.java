package site.teamo.biu.wdrop.base;

import java.util.List;
import java.util.function.Predicate;

/**
 * @author 爱做梦的锤子
 * @create 2020/11/3
 */
public interface Container<E> {
    /**
     * 从容器中取出一个元素
     * @return
     */
    E takeOut();

    /**
     * 向容器中放入一个元素
     */
    void putIn(E e);

    /**
     * 移除一个元素
     */
    E remove(E e);

    /**
     * 获取容器中所有元素
     * @return
     */
    List<E> list();

    /**
     * 根据一个过滤函数查找元素
     * @param predicate
     * @return
     */
    List<E> find(Predicate<? super E> predicate);
}
