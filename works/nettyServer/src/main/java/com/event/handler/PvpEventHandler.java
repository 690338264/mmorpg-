package com.event.handler;

import com.event.EventManager;
import com.event.model.PvpEvent;
import com.function.player.model.Player;
import com.function.quest.service.QuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Catherine
 * @create 2020-09-13 19:28
 */
@Component
public class PvpEventHandler {
    @Autowired
    private QuestService questService;

    {
        EventManager.putEvent(PvpEvent.class, this::pkWin);
    }

    private void pkWin(PvpEvent pvpEvent, Player player) {
        questService.checkQuestNoId(player, pvpEvent.getType(), 1);
    }
}
