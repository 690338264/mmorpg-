package com.event.handler.questEventHandler;

import com.event.BaseQuestEventHandler;
import com.event.EventHandler;
import com.event.EventManager;
import com.event.model.playerEvent.FriendAddEvent;
import com.function.quest.model.QuestType;
import com.function.quest.service.QuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Catherine
 * @create 2020-09-13 16:18
 */
@Component
public class BaseQuestOnFriendAddEventHandler extends BaseQuestEventHandler implements EventHandler<FriendAddEvent> {
    @Autowired
    private QuestService questService;

    {
        EventManager.putEvent(FriendAddEvent.class, this);
    }

    @Override
    public QuestType getType() {
        return QuestType.FRIEND_ADD;
    }

    @Override
    public void handle(FriendAddEvent event) {
        questService.checkQuestNoId(event.getPlayer(), getType(), 1);
    }
}
