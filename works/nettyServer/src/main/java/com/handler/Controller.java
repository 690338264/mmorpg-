package com.handler;

import io.netty.channel.ChannelHandlerContext;
import util.Msg;
/**
 * @author Catherine
 */
@FunctionalInterface
public interface Controller {

    //接收数据处理业务
    void handle(ChannelHandlerContext cxt, Msg message);

}
