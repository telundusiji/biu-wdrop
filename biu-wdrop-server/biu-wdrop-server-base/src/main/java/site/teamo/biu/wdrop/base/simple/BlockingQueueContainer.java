package site.teamo.biu.wdrop.base.simple;

import site.teamo.biu.wdrop.base.Container;

import java.util.List;
import java.util.function.Predicate;

/**
 * @author 爱做梦的锤子
 * @create 2020/11/4
 */
public class BlockingQueueContainer<E> implements Container<E> {


    @Override
    public E takeOut() {
        return null;
    }

    @Override
    public void putIn(E e) {

    }

    @Override
    public E remove(E e) {
        return null;
    }

    @Override
    public List<E> list() {
        return null;
    }

    @Override
    public List<E> find(Predicate<? super E> predicate) {
        return null;
    }
}
