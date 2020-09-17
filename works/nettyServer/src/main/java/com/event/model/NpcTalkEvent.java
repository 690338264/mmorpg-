package com.event.model;

import com.event.BasePlayerEvent;

/**
 * @author Catherine
 * @create 2020-09-13 18:09
 */
public class NpcTalkEvent extends BasePlayerEvent {

    public NpcTalkEvent(int npcId) {
        this.npcId = npcId;
    }

    private final int npcId;

    public int getNpcId() {
        return npcId;
    }
}
