package com.event;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Catherine
 * @create 2020-09-10 19:43
 */

public class EventManager {

    private static final Map<Class<? extends IEvent>, List<EventHandler>> listenerMap = new ConcurrentHashMap<>();

    public static <E extends IEvent> void putEvent(Class<? extends IEvent> eventClass, EventHandler<E> eventHandler) {
        listenerMap.computeIfAbsent(eventClass, key -> new CopyOnWriteArrayList<>());
        listenerMap.get(eventClass).add(eventHandler);
    }

    public static <E extends BasePlayerEvent> List<EventHandler> getEventList(E event) {
        return listenerMap.get(event.getClass());

    }


}
