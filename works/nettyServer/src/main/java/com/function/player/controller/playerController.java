package com.function.player.controller;

import com.Cmd;
import com.function.player.service.PlayerService;
import com.function.user.model.UserModel;
import com.function.user.service.UserService;
import com.handler.ControllerManager;
import io.netty.channel.ChannelHandlerContext;
import util.Msg;
import util.ParamNumCheck;

import javax.annotation.Resource;

public class playerController {

    @Resource
    private PlayerService playerService;

    @Resource
    private UserService userService;
    {
        ControllerManager.add(Cmd.PLAYER_CREATE,this::createRole);
    }

    private void createRole(ChannelHandlerContext ctx, Msg msg){
        UserModel userModel = userService.getUserModel();
        String[] params = ParamNumCheck.numCheck(ctx,msg,3);
        String roleName = params[1];
        Integer roleType = Integer.valueOf(params[2]);
        playerService.roleCreate(ctx,roleName,roleType,userModel.getId());
    }
}
