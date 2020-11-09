package site.teamo.biu.wdrop.base;

import java.util.List;
import java.util.function.Predicate;

/**
 * @author 爱做梦的锤子
 * @create 2020/11/3
 */
public interface Container<E> {

    String name();

    /**
     * 从容器中取出一个元素
     *
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
    void remove(E e);

    /**
     * 获取容器中所有元素
     *
     * @return
     */
    List<E> list();

    /**
     * 根据一个过滤函数查找元素
     *
     * @param predicate
     * @return
     */
    List<E> find(Predicate<? super E> predicate);

    Container NONE = new Container() {
        @Override
        public String name() {
            return null;
        }

        @Override
        public Object takeOut() {
            return null;
        }

        @Override
        public void putIn(Object o) {

        }

        @Override
        public void remove(Object o) {

        }

        @Override
        public List list() {
            return null;
        }

        @Override
        public List find(Predicate predicate) {
            return null;
        }
    };
}
