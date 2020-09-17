package com.event.handler;

import com.event.EventHandler;
import com.event.EventManager;
import com.event.QuestEvent;
import com.event.model.TeamJoinEvent;
import com.function.quest.model.QuestType;
import com.function.quest.service.QuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Catherine
 * @create 2020-09-13 19:16
 */
@Component
public class QuestOnTeamJoinEventHandler extends QuestEvent implements EventHandler<TeamJoinEvent> {
    @Autowired
    private QuestService questService;

    {
        EventManager.putEvent(TeamJoinEvent.class, this);
    }

    @Override
    public QuestType getType() {
        return QuestType.TEAM_JOIN;
    }

    @Override
    public void handle(TeamJoinEvent event) {
        questService.checkQuestNoId(event.getPlayer(), getType(), 1);
    }
}
