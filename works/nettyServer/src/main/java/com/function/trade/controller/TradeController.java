package com.function.trade.controller;

import com.Cmd;
import com.function.player.model.Player;
import com.function.trade.service.TradeService;
import com.handler.ControllerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.Msg;

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
    }

    private void createTrade(Player player, Msg msg) {

    }

}
