package com.event.handler;

import com.event.EventManager;
import com.event.model.NpcTalkEvent;
import com.function.player.model.Player;
import com.function.quest.service.QuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Catherine
 * @create 2020-09-13 18:14
 */
@Component
public class NpcTalkHandler {
    @Autowired
    private QuestService questService;

    {
        EventManager.putEvent(NpcTalkEvent.class, this::npcTalk);
    }

    private void npcTalk(NpcTalkEvent npcTalkEvent, Player player) {
        questService.checkQuestWithId(player, npcTalkEvent.getType(), npcTalkEvent.getNpcId(), npcTalkEvent.getParam());
    }
}
