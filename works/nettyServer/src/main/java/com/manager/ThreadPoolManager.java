package com.manager;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author Catherine
 * @create 2020-08-20 15:19
 */
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

    public static ScheduledExecutorService get(int id) {
        return services[id % 24];
    }
}
