package com.function.item.controller;

import com.Cmd;

import com.handler.ControllerManager;
import io.netty.channel.ChannelHandlerContext;
import util.Msg;
import util.ParamNumCheck;

import javax.annotation.Resource;

public class ItemController {
    @Resource
    private ItemController itemcontroller;
    {
        ControllerManager.add(Cmd.ITEM_USE,this::itemUse);
    }

    private void itemUse(ChannelHandlerContext ctx, Msg msg){
        String[] params = ParamNumCheck.numCheck(ctx,msg,2);
        Integer index = Integer.valueOf(params[1]);
    }
}
