package com.event.handler.questEventHandler;

import com.event.BaseQuestEventHandler;
import com.event.EventHandler;
import com.event.EventManager;
import com.event.model.playerEvent.ItemGetEvent;
import com.function.quest.model.QuestType;
import com.function.quest.service.QuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Catherine
 * @create 2020-09-13 19:01
 */
@Component
public class BaseQuestOnItemGetEventHandlerHandler extends BaseQuestEventHandler implements EventHandler<ItemGetEvent> {
    @Autowired
    private QuestService questService;

    {
        EventManager.putEvent(ItemGetEvent.class, this);
    }

    @Override
    public QuestType getType() {
        return QuestType.ITEM_GET;
    }

    @Override
    public void handle(ItemGetEvent event) {
        questService.checkQuestWithId(event.getPlayer(), getType(), event.getItemId(), event.getParam());
    }
}
