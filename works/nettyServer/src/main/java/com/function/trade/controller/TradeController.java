package com.function.trade.controller;

import com.Cmd;
import com.function.player.model.Player;
import com.function.trade.model.TradeBoard;
import com.function.trade.service.TradeService;
import com.handler.ControllerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.Msg;
import util.ParamNumCheck;

/**
 * @author Catherine
 * @create 2020-09-04 11:22
 */
@Component
public class TradeController {
    @Autowired
    private TradeService tradeService;

    {
        ControllerManager.add(Cmd.CREATE_TRADE, this::createTrade);
        ControllerManager.add(Cmd.AGREE_TRADE, this::agreeTrade);
        ControllerManager.add(Cmd.PUT_CHANGE, this::putItem);
        ControllerManager.add(Cmd.PUT_MONEY, this::putMoney);
        ControllerManager.add(Cmd.COMMIT_TRADE, this::confirmTrade);
        ControllerManager.add(Cmd.CANCEL_TRADE, this::cancelTrade);
        ControllerManager.add(Cmd.LIST_TRADE, this::listTrade);
    }

    private void createTrade(Player player, Msg msg) {
        String[] params = ParamNumCheck.numCheck(player, msg, 2);
        long playerId = Long.parseLong(params[1]);
        tradeService.requestTrade(player, playerId);
    }

    private void agreeTrade(Player player, Msg msg) {
        tradeService.agreeTrade(player);
    }

    private void putItem(Player player, Msg msg) {
        String[] params = ParamNumCheck.numCheck(player, msg, 3);
        int index = Integer.parseInt(params[1]);
        int num = Integer.parseInt(params[2]);
        tradeService.putItem(player, index, num);
    }

    private void putMoney(Player player, Msg msg) {
        String[] params = ParamNumCheck.numCheck(player, msg, 2);
        int money = Integer.parseInt(params[1]);
        tradeService.putMoney(player, money);
    }

    private void confirmTrade(Player player, Msg msg) {
        tradeService.commitTrade(player);
    }

    private void cancelTrade(Player player, Msg msg) {
        TradeBoard tradeBoard = player.getTradeBoard();
        if (tradeService.noTrading(tradeBoard, player)) {
            return;
        }
        tradeService.cancelTrade(tradeBoard);
    }

    private void listTrade(Player player, Msg msg) {
        tradeService.listTradeBoard(player);
    }
}
