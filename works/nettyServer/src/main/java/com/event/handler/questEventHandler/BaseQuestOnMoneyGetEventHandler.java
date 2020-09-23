package com.event.handler.questEventHandler;

import com.event.BaseQuestEventHandler;
import com.event.EventHandler;
import com.event.EventManager;
import com.event.model.playerEvent.MoneyGetEvent;
import com.function.quest.model.QuestType;
import com.function.quest.service.QuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Catherine
 * @create 2020-09-10 19:35
 */
@Component
public class BaseQuestOnMoneyGetEventHandler extends BaseQuestEventHandler implements EventHandler<MoneyGetEvent> {
    @Autowired
    private QuestService questService;

    {
        EventManager.putEvent(MoneyGetEvent.class, this);
    }

    @Override
    public QuestType getType() {
        return QuestType.MONEY_GET;
    }

    @Override
    public void handle(MoneyGetEvent event) {
        questService.checkQuestNoId(event.getPlayer(), getType(), event.getMoney());
    }
}
