package com.event.handler;

import com.event.EventHandler;
import com.event.EventManager;
import com.event.QuestEvent;
import com.event.model.DungeonEvent;
import com.function.quest.model.QuestType;
import com.function.quest.service.QuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Catherine
 * @create 2020-09-13 19:15
 */
@Component
public class QuestOnDungeonEventHandler extends QuestEvent implements EventHandler<DungeonEvent> {
    @Autowired
    private QuestService questService;

    {
        EventManager.putEvent(DungeonEvent.class, this);
    }

    @Override
    public QuestType getType() {
        return QuestType.DUNGEON_PASS;
    }

    @Override
    public void handle(DungeonEvent event) {
        questService.checkQuestWithId(event.getPlayer(), getType(), event.getDungeonId(), 1);
    }
}
