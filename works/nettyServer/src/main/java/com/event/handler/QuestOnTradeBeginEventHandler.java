package com.event.handler;

import com.event.EventHandler;
import com.event.EventManager;
import com.event.QuestEvent;
import com.event.model.TradeBeginEvent;
import com.function.quest.model.QuestType;
import com.function.quest.service.QuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Catherine
 * @create 2020-09-13 19:31
 */
@Component
public class QuestOnTradeBeginEventHandler extends QuestEvent implements EventHandler<TradeBeginEvent> {
    @Autowired
    private QuestService questService;

    {
        EventManager.putEvent(TradeBeginEvent.class, this);
    }

    @Override
    public QuestType getType() {
        return QuestType.TRADE;
    }

    @Override
    public void handle(TradeBeginEvent event) {
        questService.checkQuestNoId(event.getPlayer(), getType(), 1);
    }
}
