package com.manager;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Catherine
 * @create 2020-09-17 17:17
 */
public class UpdateThreadManager {

    private static final int UPDATE_TIME = 10000;
    private static final Map<String, Runnable> updateMap = new ConcurrentHashMap<>();
    private static final Map<String, Runnable> cacheMap = new ConcurrentHashMap<>();
    private static final ScheduledExecutorService updateThreadPool;

    static {
        updateThreadPool = Executors.newScheduledThreadPool(500);
        updateThreadPool.scheduleWithFixedDelay(() -> {
            cacheMap.putAll(updateMap);
            cacheMap.forEach((key, runnable) -> {
                updateMap.remove(key);
                updateThreadPool.execute(runnable);
            });
            cacheMap.clear();
        }, 0, UPDATE_TIME, TimeUnit.MILLISECONDS);

    }

    public static void putIntoThreadPool(Class<?> className, long id, Runnable runnable) {
        String key = className + "-" + id;
        updateMap.putIfAbsent(key, runnable);
    }

}
