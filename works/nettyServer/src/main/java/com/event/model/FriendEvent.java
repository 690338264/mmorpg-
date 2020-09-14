package com.event.model;

import com.event.QuestEvent;
import com.function.quest.model.QuestType;

/**
 * @author Catherine
 * @create 2020-09-13 17:01
 */
public class FriendEvent extends QuestEvent {
    @Override
    public QuestType getType() {
        return QuestType.FRIEND_ADD;
    }
}
