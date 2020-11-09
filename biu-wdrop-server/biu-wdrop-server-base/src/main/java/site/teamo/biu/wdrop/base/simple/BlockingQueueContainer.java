package site.teamo.biu.wdrop.base.simple;

import lombok.extern.slf4j.Slf4j;
import site.teamo.biu.wdrop.base.Container;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author 爱做梦的锤子
 * @create 2020/11/4
 */
@Slf4j
public class BlockingQueueContainer<E> implements Container<E> {

    private final String name;
    private final BlockingQueue<E> data;

    public BlockingQueueContainer(String name) {
        this(name, new LinkedBlockingQueue<>());
    }

    public BlockingQueueContainer(String name, BlockingQueue<E> data) {
        this.name = name;
        this.data = data;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public E takeOut() {
        try {
            return data.take();
        } catch (InterruptedException e) {
            log.warn("Blocking of fetching elements from the blocking queue container was interrupted");
            return null;
        }
    }

    @Override
    public void putIn(E e) {
        try {
            data.put(e);
        } catch (InterruptedException interruptedException) {
            log.warn("Blocking of putting elements into the blocking queue container is interrupted");
        }
    }

    @Override
    public void remove(E e) {
        data.remove(e);
    }

    @Override
    public List<E> list() {
        ArrayList<E> elements = new ArrayList<>();
        elements.addAll(data);
        return elements;
    }

    @Override
    public List<E> find(Predicate<? super E> predicate) {
        return data.stream().filter(predicate).collect(Collectors.toList());
    }
}
