package com.function.auction.controller;

import com.Cmd;
import com.function.auction.service.AuctionService;
import com.function.player.model.Player;
import com.function.scene.service.NotifyScene;
import com.handler.ControllerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.Msg;
import util.ParamNumCheck;

/**
 * @author Catherine
 * @create 2020-09-08 16:38
 */
@Component
public class AuctionController {
    @Autowired
    private AuctionService auctionService;
    @Autowired
    private NotifyScene notifyScene;
    private final static int MINUTE_MAX = 59;
    private final static int HOUR_MAX = 23;

    {
        ControllerManager.add(Cmd.LIST_FIXED, this::showFixedPrice);
        ControllerManager.add(Cmd.LIST_COMPETITION, this::showCompetition);
        ControllerManager.add(Cmd.CREATE_AUCTION, this::createAuction);
        ControllerManager.add(Cmd.BUY_FIXED, this::buyFixedAuction);
        ControllerManager.add(Cmd.JOIN_COMPETITION, this::joinCompetitionAuction);
    }

    private void showFixedPrice(Player player, Msg msg) {
        auctionService.showFixedPrice(player);
    }

    private void showCompetition(Player player, Msg msg) {
        auctionService.showCompetitionAuction(player);
    }

    private void createAuction(Player player, Msg msg) {
        String[] params = ParamNumCheck.numCheck(player, msg, 7);
        int type = Integer.parseInt(params[1]);
        int index = Integer.parseInt(params[2]);
        int num = Integer.parseInt(params[3]);
        int money = Integer.parseInt(params[4]);
        int hour = Integer.parseInt(params[5]);
        int minute = Integer.parseInt(params[6]);
        if (hour > HOUR_MAX || hour < 0 || minute > MINUTE_MAX || minute < 0) {
            notifyScene.notifyPlayer(player, "时间非法!\n");
            return;
        }
        long time = hour * 60 * 60 * 1000 + minute * 60000;
        auctionService.createAuction(player, type, index, num, money, time);
    }

    private void buyFixedAuction(Player player, Msg msg) {
        String[] params = ParamNumCheck.numCheck(player, msg, 2);
        long auctionId = Long.parseLong(params[1]);
        auctionService.buyFixedAuction(player, auctionId);
    }

    private void joinCompetitionAuction(Player player, Msg msg) {
        String[] params = ParamNumCheck.numCheck(player, msg, 3);
        long auctionId = Long.parseLong(params[1]);
        int money = Integer.parseInt(params[2]);
        auctionService.joinCompetition(player, auctionId, money);
    }
}
