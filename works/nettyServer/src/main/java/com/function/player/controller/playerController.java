package com.function.player.controller;

import com.Cmd;
import com.function.player.model.Player;
import com.function.player.service.PlayerService;
import com.function.user.model.User;
import com.function.user.service.UserService;
import com.handler.ControllerManager;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import util.Msg;
import util.ParamNumCheck;

/**
 * @author Catherine
 */
@Service
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private UserService userService;

    {
        ControllerManager.add(Cmd.PLAYER_CREATE, this::createRole);
        ControllerManager.add(Cmd.ATTACK, this::attackMonster);
        ControllerManager.add(Cmd.PLAYER_STATE, this::playerState);
    }

    private void createRole(ChannelHandlerContext ctx, Msg msg) {
        User user = userService.getUserByCtx(ctx);
        String[] params = ParamNumCheck.numCheck(ctx, msg, 3);
        String roleName = params[1];
        Integer roleType = Integer.valueOf(params[2]);
        playerService.roleCreate(ctx, roleName, roleType, user.getId());
    }

    private void attackMonster(ChannelHandlerContext ctx, Msg msg) {
        Player player = userService.getPlayerByCtx(ctx);
        String[] params = ParamNumCheck.numCheck(ctx, msg, 3);
        Integer skill = Integer.valueOf(params[1]);
        Integer target = Integer.valueOf(params[2]);
        playerService.attackMonster(player, skill, target);
    }

    private void playerState(ChannelHandlerContext ctx, Msg msg) {
        Player player = userService.getPlayerByCtx(ctx);
        playerService.showState(ctx, player);
    }
}
