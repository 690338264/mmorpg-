package com.event.model;

import com.event.QuestEvent;
import com.function.quest.model.QuestType;

/**
 * @author Catherine
 * @create 2020-09-13 16:56
 */
public class MonsterEvent extends QuestEvent {
    @Override
    public QuestType getType() {
        return QuestType.MONSTER_KILL;
    }

    public MonsterEvent(int monsterId) {
        this.monsterId = monsterId;
    }

    private final int monsterId;

    public int getMonsterId() {
        return monsterId;
    }
}
