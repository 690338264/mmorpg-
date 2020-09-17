package com.event;


/**
 * @author Catherine
 * @create 2020-09-10 19:33
 */
public interface EventHandler<E extends IEvent> {
    /**
     * 事件管理
     *
     * @param event 事件
     */
    void handle(E event);
}
