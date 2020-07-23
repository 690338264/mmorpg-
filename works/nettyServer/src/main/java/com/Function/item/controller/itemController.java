package com.Function.item.controller;

import com.Cmd;

import com.Function.Player.model.playerModel;
import com.handler.controllerManager;
import io.netty.channel.ChannelHandlerContext;
import util.Msg;
import util.ParamNumCheck;

import javax.annotation.Resource;

public class itemController {
    @Resource
    private itemController itemcontroller;
    {
        controllerManager.add(Cmd.ITEM_USE,this::itemUse);
    }

    private void itemUse(ChannelHandlerContext ctx, Msg msg){
        String[] params = ParamNumCheck.numCheck(ctx,msg,2);
        Integer index = Integer.valueOf(params[1]);
    }
}
