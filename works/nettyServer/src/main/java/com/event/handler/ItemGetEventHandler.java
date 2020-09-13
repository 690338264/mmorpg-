package com.event.handler;

import com.event.EventManager;
import com.event.model.ItemGetEvent;
import com.function.player.model.Player;
import com.function.quest.service.QuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Catherine
 * @create 2020-09-13 19:01
 */
@Component
public class ItemGetEventHandler {
    @Autowired
    private QuestService questService;

    {
        EventManager.putEvent(ItemGetEvent.class, this::getItem);
    }

    private void getItem(ItemGetEvent itemGetEvent, Player player) {
        questService.checkQuestWithId(player, itemGetEvent.getType(), itemGetEvent.getItemId(), itemGetEvent.getParam());
    }
}
