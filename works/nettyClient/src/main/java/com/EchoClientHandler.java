package com;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

@ChannelHandler.Sharable
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf>{
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext,ByteBuf byteBuf)throws Exception{
        //记录已接受消息的转储
        System.out.println("Client received:"+byteBuf.toString(CharsetUtil.UTF_8));
    }
    /*  回调函数
    @param ctx
    @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception{
        //被通知Channel活跃时发送一条消息
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!",CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause) throws Exception{
        cause.printStackTrace();
        ctx.close();
    }
}
