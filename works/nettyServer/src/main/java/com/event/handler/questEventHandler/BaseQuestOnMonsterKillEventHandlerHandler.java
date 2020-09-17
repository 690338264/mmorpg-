package com.event.handler.questEventHandler;

import com.event.BaseQuestEventHandler;
import com.event.EventHandler;
import com.event.EventManager;
import com.event.model.playerEvent.MonsterKillEvent;
import com.function.quest.model.QuestType;
import com.function.quest.service.QuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Catherine
 * @create 2020-09-13 16:19
 */
@Component
public class BaseQuestOnMonsterKillEventHandlerHandler extends BaseQuestEventHandler implements EventHandler<MonsterKillEvent> {
    @Autowired
    private QuestService questService;

    {
        EventManager.putEvent(MonsterKillEvent.class, this);
    }

    @Override
    public QuestType getType() {
        return QuestType.MONSTER_KILL;
    }

    @Override
    public void handle(MonsterKillEvent event) {
        questService.checkQuestWithId(event.getPlayer(), getType(), event.getMonsterId(), 1);
    }
}
