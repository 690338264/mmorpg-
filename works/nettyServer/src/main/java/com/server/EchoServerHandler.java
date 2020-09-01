package com.server;

import com.Cmd;
import com.function.player.model.Player;
import com.function.user.service.UserService;
import com.handler.Controller;
import com.handler.ControllerManager;
import com.handler.LoggedController;
import com.manager.ThreadPoolManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.Msg;

import javax.annotation.PostConstruct;
import java.net.InetAddress;

/**
 * @author Catherine
 */

@Slf4j
@Component
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    @Autowired
    private UserService userService;

    private static EchoServerHandler echoServerHandler;

    @PostConstruct
    private void init() {
        echoServerHandler = this;
        echoServerHandler.userService = this.userService;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ctx.writeAndFlush("server Received your message !\n");
        String cmd = msg.toString();
        String[] split = cmd.split(" ");
        String cmdIds = split[0];
        int cmdId = Integer.parseInt(cmdIds.trim());
        Controller contr = ControllerManager.getSelf().get(cmdId);
        LoggedController controller = ControllerManager.getSelf().gets(cmdId);
        Msg message = new Msg();
        message.setCmdId(cmdId);
        message.setContent(cmd);
        if (contr == null && controller == null) {
            ctx.writeAndFlush("指令错误！\n");
        } else {
            Player player = echoServerHandler.userService.getPlayerByCtx(ctx);
            if (echoServerHandler.userService.getUserByCtx(ctx) == null || player == null) {
                if (cmdId > Cmd.PLAYER_LOG.getCmdId()) {
                    ctx.writeAndFlush("请先登录！\n");
                } else {
                    ThreadPoolManager.immediateThread(() -> contr.handle(ctx, message), ctx.hashCode());
                }
            } else {
                ThreadPoolManager.immediateThread(() -> controller.handle(player, message), player.getTPlayer().getRoleId().intValue());
            }
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
        //保存数据
        Player player = echoServerHandler.userService.getPlayerByCtx(ctx);
        echoServerHandler.userService.logout(player);
        log.info("客户端已离线");
    }

}
