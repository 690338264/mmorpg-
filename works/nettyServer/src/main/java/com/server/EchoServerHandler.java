package com.server;

import com.function.user.map.PlayerMap;
import com.function.user.service.UserService;
import com.handler.Controller;
import com.handler.ControllerManager;
import com.manager.ThreadPoolManager;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private UserService userService;
    @Autowired
    private PlayerMap playerMap;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ctx.writeAndFlush("server Received your message !\n");
        String cmd = msg.toString();
        String[] split = cmd.split(" ");
        String cmdIds = split[0];
        int cmdId = Integer.parseInt(cmdIds.trim());
        Controller contr = ControllerManager.getSelf().get(cmdId);
        Msg message = new Msg();
        message.setCmdId(cmdId);
        message.setContent(cmd);
        if (userService.getPlayerByCtx(ctx).getHp() <= 0) {
            ctx.writeAndFlush("请等待复活!\n");
            return;
        }
        if (contr == null) {
            ctx.writeAndFlush("指令错误！\n");
        } else {
            ThreadPoolManager.runThread(() -> {
                contr.handle(ctx, message);
            }, 0, ctx.hashCode());
        }

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
