package com.event.handler;

import com.event.EventManager;
import com.event.model.TeamEvent;
import com.function.player.model.Player;
import com.function.quest.service.QuestService;
import com.function.team.manager.TeamManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Catherine
 * @create 2020-09-13 19:16
 */
@Component
public class TeamEventHandler {
    @Autowired
    private QuestService questService;
    @Autowired
    private TeamManager teamManager;

    {
        EventManager.putEvent(TeamEvent.class, this::teamMake);
    }

    private void teamMake(TeamEvent teamEvent, Player player) {

        questService.checkQuestNoId(player, teamEvent.getType(), 1);

    }
}
