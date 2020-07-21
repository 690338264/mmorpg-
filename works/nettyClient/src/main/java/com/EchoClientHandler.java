package com;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

//客户端逻辑


public class EchoClientHandler extends ChannelInboundHandlerAdapter{
    //监听服务器发送的数据
    @Override
    public void channelRead(ChannelHandlerContext cxt,Object msg){
        //记录已接受消息的转储
        System.out.println("Server say:"+msg.toString());
    }

    /*
    *启动客户端时触发
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception{
        System.out.println("Client active");
        super.channelActive(ctx);
    }

    /*
    关闭客户端触发
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx)throws  Exception{
        System.out.println("Client close");
        super.channelInactive(ctx);
    }
/*
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause) throws Exception{
        cause.printStackTrace();
        ctx.close();
    }
    */
}
