package com.event.handler;

import com.event.EventManager;
import com.event.model.SectEvent;
import com.function.player.model.Player;
import com.function.quest.service.QuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Catherine
 * @create 2020-09-13 19:23
 */
@Component
public class SectEventHandler {
    @Autowired
    private QuestService questService;

    {
        EventManager.putEvent(SectEvent.class, this::sectAdd);
    }

    private void sectAdd(SectEvent sectEvent, Player player) {
        questService.checkQuestNoId(player, sectEvent.getType(), 1);
    }
}
