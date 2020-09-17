package com.event.handler.questEventHandler;

import com.event.BaseQuestEventHandler;
import com.event.EventHandler;
import com.event.EventManager;
import com.event.model.playerEvent.DungeonPassEvent;
import com.function.quest.model.QuestType;
import com.function.quest.service.QuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Catherine
 * @create 2020-09-13 19:15
 */
@Component
public class BaseQuestOnDungeonPassEventHandlerHandler extends BaseQuestEventHandler implements EventHandler<DungeonPassEvent> {
    @Autowired
    private QuestService questService;

    {
        EventManager.putEvent(DungeonPassEvent.class, this);
    }

    @Override
    public QuestType getType() {
        return QuestType.DUNGEON_PASS;
    }

    @Override
    public void handle(DungeonPassEvent event) {
        questService.checkQuestWithId(event.getPlayer(), getType(), event.getDungeonId(), 1);
    }
}
