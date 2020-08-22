package com.function.communicate.controller;

import com.Cmd;
import com.function.communicate.service.CommunicateService;
import com.function.player.model.Player;
import com.function.user.service.UserService;
import com.handler.ControllerManager;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.Msg;
import util.ParamNumCheck;

/**
 * @author Catherine
 * @create 2020-08-20 17:30
 */
@Component
public class CommunicateController {
    @Autowired
    private UserService userService;
    @Autowired
    private CommunicateService communicateService;

    {
        ControllerManager.add(Cmd.WHISPER, this::whisper);
        ControllerManager.add(Cmd.SPEAK, this::talkToAll);
        ControllerManager.add(Cmd.LIST_EMAIL, this::showEmail);
        ControllerManager.add(Cmd.CHECK_EMAIL, this::showDetail);
        ControllerManager.add(Cmd.SEND_EMAIL, this::sendEmail);
        ControllerManager.add(Cmd.RECEIVE_EMAIL, this::receiveGift);
    }

    private void whisper(ChannelHandlerContext ctx, Msg msg) {
        Player player = userService.getPlayerByCtx(ctx);
        String[] params = ParamNumCheck.numCheck(ctx, msg, 3);
        Long toTalk = Long.parseLong(params[1]);
        String text = params[2];
        communicateService.whisper(player, toTalk, text);
    }

    private void talkToAll(ChannelHandlerContext ctx, Msg msg) {
        Player player = userService.getPlayerByCtx(ctx);
        String[] params = ParamNumCheck.numCheck(ctx, msg, 2);
        String text = params[1];
        communicateService.speak(player, text);
    }

    private void sendEmail(ChannelHandlerContext ctx, Msg msg) {
        Player player = userService.getPlayerByCtx(ctx);
        String[] params = ParamNumCheck.numCheck(ctx, msg, 5);
        Long playerId = Long.parseLong(params[1]);
        String text = params[2];
        String[] items = params[3].split(",");
        String[] num = params[4].split(",");
        communicateService.sendEmail(player, text, playerId, items, num);
    }

    private void showEmail(ChannelHandlerContext ctx, Msg msg) {
        Player player = userService.getPlayerByCtx(ctx);
        communicateService.showEmail(player);
    }

    private void showDetail(ChannelHandlerContext ctx, Msg msg) {
        Player player = userService.getPlayerByCtx(ctx);
        String[] params = ParamNumCheck.numCheck(ctx, msg, 2);
        int emailId = Integer.parseInt(params[1]);
        communicateService.showDetail(player, emailId);
    }

    private void receiveGift(ChannelHandlerContext ctx, Msg msg) {
        Player player = userService.getPlayerByCtx(ctx);
        String[] params = ParamNumCheck.numCheck(ctx, msg, 2);
        int emailId = Integer.parseInt(params[1]);
        communicateService.receive(player, emailId);
    }
}
