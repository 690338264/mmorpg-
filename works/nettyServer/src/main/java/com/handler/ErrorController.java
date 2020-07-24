package com.handler;

import io.netty.channel.ChannelHandlerContext;
import util.Msg;

public class ErrorController implements Controller {
    @Override
    public void handle(ChannelHandlerContext ctx, Msg message){
        ctx.writeAndFlush("请求不存在！");
    }

}
