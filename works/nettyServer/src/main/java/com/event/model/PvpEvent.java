package com.event.model;

import com.event.QuestEvent;
import com.function.quest.model.QuestType;

/**
 * @author Catherine
 * @create 2020-09-13 19:28
 */
public class PvpEvent extends QuestEvent {
    @Override
    public QuestType getType() {
        return QuestType.PK_WIN;
    }

    public int getParam() {
        return 1;
    }
}
