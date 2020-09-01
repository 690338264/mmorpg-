package com.manager;

import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author Catherine
 * @create 2020-08-20 15:19
 */
@SuppressWarnings("rawtypes")
@Component
public class ThreadPoolManager {
    private static final int SIZE = 24;
    private static ScheduledExecutorService[] services;

    static {
        ScheduledExecutorService[] services = new ScheduledExecutorService[SIZE];
        for (int i = 0; i < services.length; i++) {
            services[i] = Executors.newSingleThreadScheduledExecutor();
        }
        ThreadPoolManager.services = services;
    }

    public static void immediateThread(Runnable runnable, int id) {
        services[id % 24].execute(runnable);
    }

    public static ScheduledFuture delayThread(Runnable runnable, long delay, int id) {
        return services[id % 24].schedule(runnable, delay, TimeUnit.MILLISECONDS);
    }

    public static ScheduledFuture loopThread(Runnable runnable, long delay, long period, int id) {
        return services[id % 24].scheduleAtFixedRate(runnable, delay, period, TimeUnit.MILLISECONDS);
    }
}
