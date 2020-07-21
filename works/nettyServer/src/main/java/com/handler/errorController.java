package com.handler;

import io.netty.channel.ChannelHandlerContext;

public class errorController implements controller{
    @Override
    public void handle(ChannelHandlerContext ctx){
        ctx.writeAndFlush("请求不存在！");
    }

}
