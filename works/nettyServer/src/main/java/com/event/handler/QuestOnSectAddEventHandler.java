package com.event.handler;

import com.event.EventHandler;
import com.event.EventManager;
import com.event.QuestEvent;
import com.event.model.SectAddEvent;
import com.function.quest.model.QuestType;
import com.function.quest.service.QuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Catherine
 * @create 2020-09-13 19:23
 */
@Component
public class QuestOnSectAddEventHandler extends QuestEvent implements EventHandler<SectAddEvent> {
    @Autowired
    private QuestService questService;

    {
        EventManager.putEvent(SectAddEvent.class, this);
    }

    @Override
    public QuestType getType() {
        return QuestType.SECT_ADD;
    }

    @Override
    public void handle(SectAddEvent event) {
        questService.checkQuestNoId(event.getPlayer(), getType(), 1);
    }
}
