package com.event.handler;

import com.event.EventManager;
import com.event.model.LevelUpEvent;
import com.function.player.model.Player;
import com.function.quest.service.QuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Catherine
 * @create 2020-09-12 21:13
 */
@Component
public class LevelUpEventHandler {
    @Autowired
    private QuestService questService;

    {
        EventManager.putEvent(LevelUpEvent.class, this::levelUp);
    }

    private void levelUp(LevelUpEvent levelUpEvent, Player player) {
        questService.checkQuestNoId(player, levelUpEvent.getType(), levelUpEvent.getParam());
    }
}
