package com.event.handler.questEventHandler;

import com.event.BaseQuestEventHandler;
import com.event.EventHandler;
import com.event.EventManager;
import com.event.model.playerEvent.LevelUpEvent;
import com.function.quest.model.QuestType;
import com.function.quest.service.QuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Catherine
 * @create 2020-09-12 21:13
 */
@Component
public class BaseQuestOnLevelUpEventHandlerHandler extends BaseQuestEventHandler implements EventHandler<LevelUpEvent> {
    @Autowired
    private QuestService questService;

    {
        EventManager.putEvent(LevelUpEvent.class, this);
    }

    @Override
    public QuestType getType() {
        return QuestType.LEVEL_UP;
    }

    @Override
    public void handle(LevelUpEvent event) {
        questService.checkQuestNoId(event.getPlayer(), getType(), 1);
        questService.checkCanAcceptByLevel(event.getPlayer());
    }
}
