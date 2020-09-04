package com.function.trade.model;

import com.function.item.model.Item;
import com.function.player.model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Catherine
 * @create 2020-09-03 20:17
 */
public class TradeBoard {
    /**
     * 交易发起者
     */
    private Player initiator;
    /**
     * 交易接收者
     */
    private Player recipient;
    /**
     * 交易开始时间
     */
    private Long startTime;
    /**
     * 物品交易栏
     */
    private Map<Long, List<Item>> changeMap = new ConcurrentHashMap<>();
    /**
     * 金币交易
     */
    private Map<Long, Integer> moneyMap = new ConcurrentHashMap<>();
    /**
     * 双方确认状态
     */
    private Map<Long, Boolean> stateMap = new ConcurrentHashMap<>();

    public TradeBoard(Player initiator, Player recipient) {
        this.initiator = initiator;
        this.recipient = recipient;
        this.startTime = System.currentTimeMillis();
        this.changeMap.put(initiator.getTPlayer().getRoleId(), new ArrayList<>());
        this.changeMap.put(recipient.getTPlayer().getRoleId(), new ArrayList<>());
        this.moneyMap.put(initiator.getTPlayer().getRoleId(), 0);
        this.moneyMap.put(recipient.getTPlayer().getRoleId(), 0);
    }

    public Player getInitiator() {
        return initiator;
    }

    public Player getRecipient() {
        return recipient;
    }

    public Long getStartTime() {
        return startTime;
    }

    public Map<Long, List<Item>> getChangeMap() {
        return changeMap;
    }

    public Map<Long, Integer> getMoneyMap() {
        return moneyMap;
    }

    public Map<Long, Boolean> getStateMap() {
        return stateMap;
    }
}
