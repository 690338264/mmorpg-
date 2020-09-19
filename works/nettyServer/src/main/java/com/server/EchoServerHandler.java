package com.server;

import com.Cmd;
import com.function.player.model.Player;
import com.function.scene.model.SceneObjectState;
import com.handler.Controller;
import com.handler.ControllerManager;
import com.handler.LoggedInController;
import com.manager.ThreadPoolManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import util.Msg;

/**
 * @author Catherine
 */

@Slf4j
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        String cmd = msg.toString();
        String[] split = cmd.split(" ");
        String cmdIds = split[0];
        int cmdId = Integer.parseInt(cmdIds.trim());
        Controller contr = ControllerManager.getSelf().get(cmdId);
        LoggedInController controller = ControllerManager.getSelf().gets(cmdId);
        Msg message = new Msg();
        message.setCmdId(cmdId);
        message.setContent(cmd);
        if (contr == null && controller == null) {
            ctx.writeAndFlush("指令错误！\n");
        } else {
            Player player = ControllerManager.getUserService().getPlayerByCtx(ctx);
            if (ControllerManager.getUserService().getPlayerByCtx(ctx) == null || player == null) {
                if (cmdId > Cmd.PLAYER_LOG.getCmdId()) {
                    ctx.writeAndFlush("请先登录！\n");
                } else {
                    ThreadPoolManager.immediateThread(() -> {
                        assert contr != null;
                        contr.handle(ctx, message);
                    }, ctx.hashCode());
                }
            } else {
                if (player.getState() == SceneObjectState.DEATH) {
                    ctx.writeAndFlush("阵亡请等待复活\n");
                    return;
                }
                if (player.getState() == SceneObjectState.DIZZY) {
                    ctx.writeAndFlush("眩晕中........\n");
                    return;
                }
                ThreadPoolManager.immediateThread(() -> controller.handle(player, message), player.getTPlayer().getRoleId().intValue());
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("RamoteAddress:" + ctx.channel().remoteAddress() + "active!");
        ctx.writeAndFlush("欢迎来到美丽的大荒!\n");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        //保存数据
        Player player = ControllerManager.getUserService().getPlayerByCtx(ctx);
        ControllerManager.getUserService().logout(player);
        log.info("客户端已离线");
    }

}
