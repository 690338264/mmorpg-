package com.function.player.controller;

import com.Cmd;
import com.function.player.model.Player;
import com.function.player.service.PlayerService;
import com.function.scene.model.SceneObjectType;
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
        ControllerManager.add(Cmd.PVP, this::pvp);
    }

    private void createRole(ChannelHandlerContext ctx, Msg msg) {
        User user = userService.getUserByCtx(ctx);
        String[] params = ParamNumCheck.numCheck(ctx, msg, 3);
        String roleName = params[1];
        Integer roleType = Integer.valueOf(params[2]);
        playerService.roleCreate(ctx, roleName, roleType, user.getId());
    }

    private void attackMonster(Player player, Msg msg) {
        String[] params = ParamNumCheck.numCheck(player, msg, 3);
        int skill = Integer.parseInt(params[1]);
        long target = Long.parseLong(params[2]);
        playerService.useSkill(player, skill, target, SceneObjectType.MONSTER);
    }

    private void pvp(Player player, Msg msg) {
        String[] params = ParamNumCheck.numCheck(player, msg, 3);
        int skill = Integer.parseInt(params[1]);
        long target = Long.parseLong(params[2]);
        playerService.useSkill(player, skill, target, SceneObjectType.PLAYER);
    }

    private void playerState(Player player, Msg msg) {
        playerService.showState(player);
    }
}
