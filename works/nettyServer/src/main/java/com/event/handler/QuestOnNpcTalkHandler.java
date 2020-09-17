package com.event.handler;

import com.event.EventHandler;
import com.event.EventManager;
import com.event.QuestEvent;
import com.event.model.NpcTalkEvent;
import com.function.quest.model.QuestType;
import com.function.quest.service.QuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Catherine
 * @create 2020-09-13 18:14
 */
@Component
public class QuestOnNpcTalkHandler extends QuestEvent implements EventHandler<NpcTalkEvent> {
    @Autowired
    private QuestService questService;

    {
        EventManager.putEvent(NpcTalkEvent.class, this);
    }

    @Override
    public QuestType getType() {
        return QuestType.NPC_TALK;
    }

    @Override
    public void handle(NpcTalkEvent event) {
        questService.checkQuestWithId(event.getPlayer(), getType(), event.getNpcId(), 1);
    }
}
