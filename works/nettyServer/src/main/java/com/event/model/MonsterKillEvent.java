package com.event.model;

import com.event.BasePlayerEvent;

/**
 * @author Catherine
 * @create 2020-09-13 16:56
 */
public class MonsterKillEvent extends BasePlayerEvent {

    public MonsterKillEvent(int monsterId) {
        this.monsterId = monsterId;
    }

    private final int monsterId;

    public int getMonsterId() {
        return monsterId;
    }
}
