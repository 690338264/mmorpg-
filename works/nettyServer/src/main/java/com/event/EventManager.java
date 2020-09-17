package com.event;

import com.manager.ThreadPoolManager;

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

    public static <E extends BasePlayerEvent> List<EventHandler> getPlayerEventList(E event) {
        return listenerMap.get(event.getClass());
    }

    public static <E extends IEvent> void submitEvent(E event) {
        listenerMap.get(event.getClass()).forEach(eventHandler -> {
            try {
                ThreadPoolManager.immediateThread(() -> eventHandler.handle(event), event.hashCode());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
