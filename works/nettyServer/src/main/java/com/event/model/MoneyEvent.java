package com.event.model;

import com.event.QuestEvent;
import com.function.quest.model.QuestType;

/**
 * @author Catherine
 * @create 2020-09-10 19:35
 */
public class MoneyEvent extends QuestEvent {
    @Override
    public QuestType getType() {
        return QuestType.MONEY_GET;
    }

    public MoneyEvent(int money) {
        this.money = money;
    }

    private final int money;

    public int getMoney() {
        return money;
    }

}
