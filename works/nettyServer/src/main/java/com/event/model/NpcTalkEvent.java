package com.event.model;

import com.event.QuestEvent;
import com.function.quest.model.QuestType;

/**
 * @author Catherine
 * @create 2020-09-13 18:09
 */
public class NpcTalkEvent extends QuestEvent {
    @Override
    public QuestType getType() {
        return QuestType.NPC_TALK;
    }

    public NpcTalkEvent(int npcId) {
        this.npcId = npcId;
    }

    private final int npcId;

    public int getNpcId() {
        return npcId;
    }

    public int getParam() {
        return 1;
    }
}
