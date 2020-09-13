package com.event.model;

import com.event.QuestEvent;
import com.function.quest.model.QuestType;

/**
 * @author Catherine
 * @create 2020-09-13 19:31
 */
public class TradeEvent extends QuestEvent {
    @Override
    public QuestType getType() {
        return QuestType.TRADE;
    }

    public int getParam() {
        return 1;
    }
}
