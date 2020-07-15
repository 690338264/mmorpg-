package com;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.net.InetAddress;
import java.util.logging.Logger;
//对数据的处理
@ChannelHandler.Sharable

public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    private  Logger logger = Logger.getLogger("com.EchoServerHandler");

    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception{
        //ByteBuf in = (ByteBuf) msg;
        System.out.println(ctx.channel().remoteAddress()+"Say:"+msg);
        /*将收到的消息写给发送者而不冲刷出站消息
        ctx.write(in);*/
        ctx.writeAndFlush("serve Received your message!\n");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx)throws Exception{
        System.out.println("RamoteAddress:"+ctx.channel().remoteAddress()+"active!");
        ctx.writeAndFlush("Welcome to "+ InetAddress.getLocalHost().getHostName()+"service!\n");//回复
        super.channelActive(ctx);
    }
    /*@Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception{
        //将未决消息冲刷到远程节点  并关闭该channel
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause)
        throws Exception{
        cause.printStackTrace();
        ctx.close();
    }*/
}
