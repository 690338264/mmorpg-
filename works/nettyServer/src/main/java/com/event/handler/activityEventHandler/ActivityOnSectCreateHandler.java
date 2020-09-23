package com.event.handler.activityEventHandler;

import com.event.EventHandler;
import com.event.EventManager;
import com.event.model.ordinaryEvent.SectCreateEvent;

/**
 * @author Catherine
 * @create 2020-09-17 21:04
 */
public class ActivityOnSectCreateHandler implements EventHandler<SectCreateEvent> {
    {
        EventManager.putEvent(SectCreateEvent.class, this);
    }

    @Override
    public void handle(SectCreateEvent event) {
    }
}
