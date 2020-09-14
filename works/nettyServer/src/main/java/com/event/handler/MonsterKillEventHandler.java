package com.event.handler;

import com.event.EventManager;
import com.event.model.MonsterEvent;
import com.function.player.model.Player;
import com.function.quest.service.QuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Catherine
 * @create 2020-09-13 16:19
 */
@Component
public class MonsterKillEventHandler {
    @Autowired
    private QuestService questService;

    {
        EventManager.putEvent(MonsterEvent.class, this::killMonster);
    }

    private void killMonster(MonsterEvent monsterEvent, Player player) {
        questService.checkQuestWithId(player, monsterEvent.getType(), monsterEvent.getMonsterId(), 1);
    }
}
