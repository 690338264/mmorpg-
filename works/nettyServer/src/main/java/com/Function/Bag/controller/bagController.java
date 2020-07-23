package com.Function.Bag.controller;

import com.Cmd;
import com.Function.Bag.service.bagService;
import com.handler.controllerManager;
import io.netty.channel.ChannelHandlerContext;
import util.Msg;

import javax.annotation.Resource;

public class bagController {

    @Resource
    private bagService bagservice;

    {
        controllerManager.add(Cmd.BAG_LIST,this::baglist);
        controllerManager.add(Cmd.BAG_INORDER,this::baginOrder);
    }

    private void baglist(ChannelHandlerContext ctx, Msg msg){
        
    }

    private void baginOrder(ChannelHandlerContext ctx,Msg msg){

    }

}
