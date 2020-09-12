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

    private int money;

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
