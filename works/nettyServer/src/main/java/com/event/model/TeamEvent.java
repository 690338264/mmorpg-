package com.event.model;

import com.event.QuestEvent;
import com.function.quest.model.QuestType;

/**
 * @author Catherine
 * @create 2020-09-13 19:16
 */
public class TeamEvent extends QuestEvent {
    @Override
    public QuestType getType() {
        return QuestType.TEAM_MAKE;
    }

    public int getParam() {
        return 1;
    }
}
