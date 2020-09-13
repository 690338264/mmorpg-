package com.event.handler;

import com.event.EventManager;
import com.event.model.MoneyEvent;
import com.function.player.model.Player;
import com.function.quest.service.QuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Catherine
 * @create 2020-09-10 19:35
 */
@Component
public class MoneyEventHandler {
    @Autowired
    private QuestService questService;

    {
        EventManager.putEvent(MoneyEvent.class, this::achieveMoney);
    }

    private void achieveMoney(MoneyEvent moneyEvent, Player player) {
    }

}
