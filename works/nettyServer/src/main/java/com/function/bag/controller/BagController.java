package com.function.bag.controller;

import com.Cmd;
import com.function.bag.service.BagService;
import com.handler.ControllerManager;
import io.netty.channel.ChannelHandlerContext;
import util.Msg;

import javax.annotation.Resource;

public class BagController {

    @Resource
    private BagService bagservice;

    {
        ControllerManager.add(Cmd.BAG_LIST,this::baglist);
        ControllerManager.add(Cmd.BAG_INORDER,this::baginOrder);
    }

    private void baglist(ChannelHandlerContext ctx, Msg msg){

    }

    private void baginOrder(ChannelHandlerContext ctx,Msg msg){

    }

}
