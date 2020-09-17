package com.event.model;

import com.event.BasePlayerEvent;

/**
 * @author Catherine
 * @create 2020-09-13 19:16
 */
public class DungeonEvent extends BasePlayerEvent {

    public DungeonEvent(int dungeonId) {
        this.dungeonId = dungeonId;
    }

    private final int dungeonId;

    public int getDungeonId() {
        return dungeonId;
    }
}
