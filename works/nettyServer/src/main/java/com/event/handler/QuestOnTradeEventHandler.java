package com.event.handler;

import com.event.EventHandler;
import com.event.EventManager;
import com.event.QuestEvent;
import com.event.model.TradeEvent;
import com.function.quest.model.QuestType;
import com.function.quest.service.QuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Catherine
 * @create 2020-09-13 19:31
 */
@Component
public class QuestOnTradeEventHandler extends QuestEvent implements EventHandler<TradeEvent> {
    @Autowired
    private QuestService questService;

    {
        EventManager.putEvent(TradeEvent.class, this);
    }

    @Override
    public QuestType getType() {
        return QuestType.TRADE;
    }

    @Override
    public void handle(TradeEvent event) {
        questService.checkQuestNoId(event.getPlayer(), getType(), 1);
    }
}
