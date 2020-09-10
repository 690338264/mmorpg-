package com.event;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Catherine
 * @create 2020-09-10 19:43
 */
public class EventManager {
    private final Map<Class<? extends Event>, List<EventHandler<?>>> map = new ConcurrentHashMap<>();

}
