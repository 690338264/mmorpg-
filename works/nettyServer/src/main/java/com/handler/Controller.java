package com.handler;

import io.netty.channel.ChannelHandlerContext;
import util.Msg;

@FunctionalInterface
public interface Controller {

    //接收数据处理业务
    void handle(ChannelHandlerContext cxt, Msg message);

}
