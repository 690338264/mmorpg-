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
@Component
public class ThreadPoolManager {
    public static final int SIZE = 24;
    public static ScheduledExecutorService[] services;

    static {
        ScheduledExecutorService[] services = new ScheduledExecutorService[SIZE];
        for (int i = 0; i < services.length; i++) {
            services[i] = Executors.newSingleThreadScheduledExecutor();
        }
        ThreadPoolManager.services = services;
    }

    public static void runThread(Runnable r, long l, int id) {
        services[id % 24].schedule(r, l, TimeUnit.MILLISECONDS);
    }

    public static ScheduledFuture loopThread(Runnable r, long delay, long period, int id) {
        ScheduledFuture scheduledFutures = services[id % 24].scheduleAtFixedRate(r, delay, period, TimeUnit.MILLISECONDS);
        return scheduledFutures;
    }
}
