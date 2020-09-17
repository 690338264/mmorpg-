package com.event.handler.questEventHandler;

import com.event.BaseQuestEventHandler;
import com.event.EventHandler;
import com.event.EventManager;
import com.event.model.playerEvent.PvpWinEvent;
import com.function.quest.model.QuestType;
import com.function.quest.service.QuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Catherine
 * @create 2020-09-13 19:28
 */
@Component
public class BaseQuestOnPvpWinEventHandlerHandler extends BaseQuestEventHandler implements EventHandler<PvpWinEvent> {
    @Autowired
    private QuestService questService;

    {
        EventManager.putEvent(PvpWinEvent.class, this);
    }

    @Override
    public QuestType getType() {
        return QuestType.PK_WIN;
    }

    @Override
    public void handle(PvpWinEvent event) {
        questService.checkQuestNoId(event.getPlayer(), getType(), 1);
    }
}
