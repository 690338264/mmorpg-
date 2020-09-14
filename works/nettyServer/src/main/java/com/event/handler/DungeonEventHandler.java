package com.event.handler;

import com.event.EventManager;
import com.event.model.DungeonEvent;
import com.function.player.model.Player;
import com.function.quest.service.QuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Catherine
 * @create 2020-09-13 19:15
 */
@Component
public class DungeonEventHandler {
    @Autowired
    private QuestService questService;

    {
        EventManager.putEvent(DungeonEvent.class, this::dungeonPass);
    }

    private void dungeonPass(DungeonEvent dungeonEvent, Player player) {
        questService.checkQuestWithId(player, dungeonEvent.getType(), dungeonEvent.getDungeonId(), 1);
    }
}
