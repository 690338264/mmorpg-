package com.event;

/**
 * @author Catherine
 * @create 2020-09-10 19:33
 */
public interface EventHandler<E extends Event> {
    void handle(E event);
}
