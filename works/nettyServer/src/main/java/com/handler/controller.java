package com.handler;

import io.netty.channel.ChannelHandlerContext;
import util.Msg;

@FunctionalInterface
public interface controller {

    //接收数据处理业务
    void handle(ChannelHandlerContext cxt, Msg message);

}
