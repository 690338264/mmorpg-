package com.server;

import com.handler.Controller;
import com.handler.ControllerManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import util.Msg;

import java.net.InetAddress;

/**
 * @author Catherine
 */

@Slf4j
@ChannelHandler.Sharable
@Component
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ctx.writeAndFlush("server Received your message !\n");

        Channel ch = ctx.channel();
        String cmd = msg.toString();
        String[] split = cmd.split(" ");
        String cmdIds = split[0];
        int cmdId = Integer.parseInt(cmdIds.trim());
        Controller contr = ControllerManager.getSelf().get(cmdId);
        Msg message = new Msg();
        message.setCmdId(cmdId);
        message.setContent(cmd);
//        if (contr == null){
//            ErrorController errorController = new ErrorController();
//            errorController.handle(ctx,message);
//        }else{
        contr.handle(ctx, message);
//        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("RamoteAddress:" + ctx.channel().remoteAddress() + "active!");
        ctx.writeAndFlush("Welcome to " + InetAddress.getLocalHost().getHostName() + "service!\n");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush("正在断开连接\n");
        //保存数据
        log.info("客户端已离线");
    }

}
