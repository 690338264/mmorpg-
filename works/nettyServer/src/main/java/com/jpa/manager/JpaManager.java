package com.jpa.manager;

import com.manager.ThreadPoolManager;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledFuture;

/**
 * @author Catherine
 * @create 2020-09-07 16:41
 */
@Component

public class JpaManager {

    public static final long UPDATE_TIME = 10000;

    public ScheduledFuture<?> update(ScheduledFuture<?> task, Runnable runnable, int id) {
        if (task == null) {
            return ThreadPoolManager.delayThread(runnable, UPDATE_TIME, id);
        }
        return null;
    }
}
