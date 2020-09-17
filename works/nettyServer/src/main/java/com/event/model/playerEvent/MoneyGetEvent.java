package com.event.model.playerEvent;

import com.event.BasePlayerEvent;

/**
 * @author Catherine
 * @create 2020-09-10 19:35
 */
public class MoneyGetEvent extends BasePlayerEvent {

    public MoneyGetEvent(int money) {
        this.money = money;
    }

    private final int money;

    public int getMoney() {
        return money;
    }
}
