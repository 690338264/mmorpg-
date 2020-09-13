package com.event.handler;

import com.event.EventManager;
import com.event.model.TradeEvent;
import com.function.player.model.Player;
import com.function.quest.service.QuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Catherine
 * @create 2020-09-13 19:31
 */
@Component
public class TradeEventHandler {
    @Autowired
    private QuestService questService;

    {
        EventManager.putEvent(TradeEvent.class, this::trade);
    }

    private void trade(TradeEvent tradeEvent, Player player) {
        questService.checkQuestNoId(player, tradeEvent.getType(), tradeEvent.getParam());
    }
}
