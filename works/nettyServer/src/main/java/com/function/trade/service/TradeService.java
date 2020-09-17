package com.function.trade.service;

import com.event.model.playerEvent.TradeBeginEvent;
import com.function.item.model.Item;
import com.function.item.model.ItemType;
import com.function.item.service.ItemService;
import com.function.player.model.Player;
import com.function.player.service.PlayerService;
import com.function.scene.model.Scene;
import com.function.scene.model.SceneObjectType;
import com.function.scene.service.NotifyScene;
import com.function.trade.manager.TradeManager;
import com.function.trade.model.TradeBoard;
import com.jpa.entity.TPlayer;
import com.manager.ThreadPoolManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;

/**
 * @author Catherine
 * @create 2020-09-04 11:22
 */
@Component
public class TradeService {
    @Autowired
    private NotifyScene notifyScene;
    @Autowired
    private TradeManager tradeManager;
    @Autowired
    private ItemService itemService;
    @Autowired
    private PlayerService playerService;

    private static final int TRADE_PEOPLE = 2;

    /**
     * 发起交易
     */
    public void requestTrade(Player player, Long playerId) {
        Scene scene = player.getNowScene();
        Player receiver = (Player) scene.getSceneObjectMap().get(SceneObjectType.PLAYER).get(playerId);
        if (receiver == null) {
            notifyScene.notifyPlayer(player, "发起交易失败，请面对面发起交易\n");
            return;
        }
        if (player.getTradeBoard() != null || tradeManager.getTradeBoardMap().containsKey(playerId)) {
            notifyScene.notifyPlayer(player, "可能仍在交易中\n");
            return;
        }
        if (player == receiver) {
            notifyScene.notifyPlayer(player, "不能和自己交易！\n");
            return;
        }
        TradeBoard tradeBoard = new TradeBoard(player, receiver);
        player.setTradeBoard(tradeBoard);
        tradeManager.getTradeBoardMap().put(playerId, tradeBoard);
        notifyScene.notifyPlayer(player, "交易邀请发起成功\n");
        notifyScene.notifyPlayer(receiver, "您收到一条交易请求\n");
    }

    /**
     * 同意交易
     */
    public void agreeTrade(Player player) {
        TradeBoard trade = tradeManager.getTradeBoardMap().get(player.getTPlayer().getRoleId());
        if (trade == null) {
            notifyScene.notifyPlayer(player, "您没有交易邀请\n");
            return;
        }
        player.setTradeBoard(trade);
        notifyScene.notifyPlayer(player, "开始交易\n");
        notifyScene.notifyPlayer(trade.getInitiator(), "玩家接受交易 请开始交易\n");
        player.asynchronousSubmitEvent(new TradeBeginEvent());
        trade.getInitiator().asynchronousSubmitEvent(new TradeBeginEvent());
    }

    /**
     * 添加交易物品
     */
    public void putItem(Player player, int index, int num) {
        Item item = player.getBag().getItemMap().get(index);
        TradeBoard tradeBoard = player.getTradeBoard();
        List<Item> itemList = tradeBoard.getChangeMap().get(player.getTPlayer().getRoleId());
        if (noTrading(tradeBoard, player)) {
            return;
        }
        if (!itemService.removeItem(item.getItemId(), index, num, player)) {
            return;
        }
        if (item.getItemById().getType() == ItemType.MEDICINAL.getType()) {

            itemList.add(itemService.copyItem(item, num));
        } else {
            itemList.add(item);
        }
        notifyScene.notifyPlayer(player, "物品添加成功\n");

    }

    /**
     * 交易金币
     */
    public void putMoney(Player player, int money) {
        TradeBoard tradeBoard = player.getTradeBoard();
        if (noTrading(tradeBoard, player)) {
            return;
        }
        if (!itemService.subMoney(player, money)) {
            return;
        }
        TPlayer tPlayer = player.getTPlayer();
        tPlayer.setMoney(tPlayer.getMoney() + tradeBoard.getMoneyMap().get(tPlayer.getRoleId()));
        tradeBoard.getMoneyMap().put(tPlayer.getRoleId(), money);
        notifyScene.notifyPlayer(player, "交易金币更新成功\n");
    }

    /**
     * 查看交易面板
     */
    public void listTradeBoard(Player player) {
        TradeBoard tradeBoard = player.getTradeBoard();
        if (noTrading(tradeBoard, player)) {
            return;
        }
        Long initiatorId = tradeBoard.getInitiator().getTPlayer().getRoleId();
        Long recipientId = tradeBoard.getRecipient().getTPlayer().getRoleId();
        notifyScene.notifyPlayer(player, "交易栏:\n发起者:\n物品:\n");
        tradeBoard.getChangeMap().get(initiatorId).forEach((item) ->
                notifyScene.notifyPlayer(player, MessageFormat.format("{0} [{1}]\n",
                        item.getItemById().getName(), item.getNum())));
        notifyScene.notifyPlayer(player, MessageFormat.format("金币:{0}\n接收者:\n",
                tradeBoard.getMoneyMap().get(initiatorId)));
        tradeBoard.getChangeMap().get(recipientId).forEach((item) ->
                notifyScene.notifyPlayer(player, MessageFormat.format("{0} [{1}]\n",
                        item.getItemById().getName(), item.getNum())));
        notifyScene.notifyPlayer(player, MessageFormat.format("金币:{0}\n",
                tradeBoard.getMoneyMap().get(recipientId)));
    }

    /**
     * 确认交易
     */
    public void commitTrade(Player player) {
        TradeBoard tradeBoard = player.getTradeBoard();
        if (noTrading(tradeBoard, player)) {
            return;
        }
        tradeBoard.getStateMap().putIfAbsent(player.getTPlayer().getRoleId(), true);
        if (tradeBoard.getStateMap().size() == TRADE_PEOPLE) {
            tradeBoard.getIsTrading().set(true);
            startTrading(tradeBoard);
            tradeBoard.getIsTrading().set(false);
        }
    }

    /**
     * 取消交易
     */
    public void cancelTrade(TradeBoard tradeBoard) {
        Player initiator = tradeBoard.getInitiator();
        Player recipient = tradeBoard.getRecipient();
        ThreadPoolManager.immediateThread(() ->
                cancelOne(initiator, tradeBoard), initiator.getTPlayer().getRoleId().intValue());
        ThreadPoolManager.immediateThread(() ->
                cancelOne(recipient, tradeBoard), recipient.getTPlayer().getRoleId().intValue());
        tradeManager.getTradeBoardMap().remove(recipient.getTPlayer().getRoleId());
    }

    /**
     * 开始交易
     */
    public void startTrading(TradeBoard tradeBoard) {
        Player initiator = tradeBoard.getInitiator();
        Player recipient = tradeBoard.getRecipient();
        Long initiatorId = initiator.getTPlayer().getRoleId();
        Long recipientId = recipient.getTPlayer().getRoleId();
        getChange(tradeBoard.getChangeMap().get(initiatorId), tradeBoard.getMoneyMap().get(initiatorId), recipient);
        getChange(tradeBoard.getChangeMap().get(recipientId), tradeBoard.getMoneyMap().get(recipientId), initiator);
        notifyScene.notifyPlayer(initiator, "交易完成\n");
        notifyScene.notifyPlayer(recipient, "交易完成\n");
        tradeBoard.getStateMap().clear();
    }

    /**
     * 逐个取消
     */
    public void cancelOne(Player player, TradeBoard tradeBoard) {
        Long playerId = player.getTPlayer().getRoleId();
        getChange(tradeBoard.getChangeMap().get(playerId), tradeBoard.getMoneyMap().get(playerId), player);
        player.setTradeBoard(null);
        notifyScene.notifyPlayer(player, "交易取消\n");
    }

    /**
     * 得到交易物品
     */
    public void getChange(List<Item> items, int money, Player player) {
        ThreadPoolManager.immediateThread(() -> {
            itemService.getItem(player, items);
            playerService.getMoney(player, money);
        }, player.getTPlayer().getRoleId().intValue());

    }

    /**
     * 玩家没在交易中
     */
    public boolean noTrading(TradeBoard tradeBoard, Player player) {
        if (tradeBoard.getRecipient().getTradeBoard() == null) {
            notifyScene.notifyPlayer(player, "没有交易在进行\n");
            return true;
        }
        return false;
    }
}
