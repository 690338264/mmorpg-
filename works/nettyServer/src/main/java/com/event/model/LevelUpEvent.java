package com.event.model;

import com.event.QuestEvent;
import com.function.quest.model.QuestType;

/**
 * @author Catherine
 * @create 2020-09-12 21:13
 */
public class LevelUpEvent extends QuestEvent {
    @Override
    public QuestType getType() {
        return QuestType.LEVEL_UP;
    }

    private final int param = 1;

    public int getParam() {
        return param;
    }
}
