package com.event.handler;

import com.event.EventHandler;
import com.event.EventManager;
import com.event.QuestEvent;
import com.event.model.PvpEvent;
import com.function.quest.model.QuestType;
import com.function.quest.service.QuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Catherine
 * @create 2020-09-13 19:28
 */
@Component
public class QuestOnPvpEventHandler extends QuestEvent implements EventHandler<PvpEvent> {
    @Autowired
    private QuestService questService;

    {
        EventManager.putEvent(PvpEvent.class, this);
    }

    @Override
    public QuestType getType() {
        return QuestType.PK_WIN;
    }

    @Override
    public void handle(PvpEvent event) {
        questService.checkQuestNoId(event.getPlayer(), getType(), 1);
    }
}
