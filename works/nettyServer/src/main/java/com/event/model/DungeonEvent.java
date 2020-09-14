package com.event.model;

import com.event.QuestEvent;
import com.function.quest.model.QuestType;

/**
 * @author Catherine
 * @create 2020-09-13 19:16
 */
public class DungeonEvent extends QuestEvent {
    @Override
    public QuestType getType() {
        return QuestType.DUNGEON_PASS;
    }

    public DungeonEvent(int dungeonId) {
        this.dungeonId = dungeonId;
    }

    private final int dungeonId;

    public int getDungeonId() {
        return dungeonId;
    }
}
