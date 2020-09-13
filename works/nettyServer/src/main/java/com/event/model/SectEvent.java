package com.event.model;

import com.event.QuestEvent;
import com.function.quest.model.QuestType;

/**
 * @author Catherine
 * @create 2020-09-13 19:24
 */
public class SectEvent extends QuestEvent {
    @Override
    public QuestType getType() {
        return QuestType.SECT_ADD;
    }

    public int getParam() {
        return 1;
    }
}
