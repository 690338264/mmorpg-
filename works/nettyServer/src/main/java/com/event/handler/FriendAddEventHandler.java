package com.event.handler;

import com.event.EventManager;
import com.event.model.FriendEvent;
import com.function.player.model.Player;
import com.function.quest.service.QuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Catherine
 * @create 2020-09-13 16:18
 */
@Component
public class FriendAddEventHandler {
    @Autowired
    private QuestService questService;

    {
        EventManager.putEvent(FriendEvent.class, this::friendAdd);
    }

    private void friendAdd(FriendEvent friendEvent, Player player) {
        questService.checkQuestNoId(player, friendEvent.getType(), friendEvent.getParam());
    }
}
