package com.function.scene.controller;

import com.Cmd;
import com.function.player.model.Player;
import com.function.scene.service.InstanceService;
import com.function.user.service.UserService;
import com.handler.ControllerManager;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.Msg;
import util.ParamNumCheck;

/**
 * @author Catherine
 * @create 2020-08-28 17:00
 */
@Component
public class InstanceController {
    @Autowired
    private UserService userService;
    @Autowired
    private InstanceService instanceService;

    {
        ControllerManager.add(Cmd.LIST_INSTANCE, this::listInstance);
        ControllerManager.add(Cmd.PERSONAL_INSTANCE, this::personalInstance);
        ControllerManager.add(Cmd.TEAM_INSTANCE, this::teamInstance);
        ControllerManager.add(Cmd.INTO_INSTANCE, this::intoInstance);
    }

    private void listInstance(ChannelHandlerContext ctx, Msg msg) {
        Player player = userService.getPlayerByCtx(ctx);
        instanceService.listInstance(player);
    }

    private void personalInstance(ChannelHandlerContext ctx, Msg msg) {
        Player player = userService.getPlayerByCtx(ctx);
        String[] params = ParamNumCheck.numCheck(ctx, msg, 2);
        int instanceId = Integer.parseInt(params[1]);
        instanceService.personalCreate(player, instanceId);
    }

    private void teamInstance(ChannelHandlerContext ctx, Msg msg) {
        Player player = userService.getPlayerByCtx(ctx);
        String[] params = ParamNumCheck.numCheck(ctx, msg, 2);
        int instanceId = Integer.parseInt(params[1]);
        instanceService.teamCreate(player, instanceId);
    }

    private void intoInstance(ChannelHandlerContext ctx, Msg msg) {
        Player player = userService.getPlayerByCtx(ctx);
        instanceService.intoInstance(player);
    }
}
