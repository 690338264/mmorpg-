package com.function.scene.service;

import com.function.player.model.PlayerModel;
import com.function.scene.model.Scene;
import com.function.user.map.PlayerMap;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Catherine
 */
@Service
public class NotifyScene {
    @Autowired
    private PlayerMap playerMap;

    public void notifyScene(Scene scene, StringBuilder notify) {
        scene.getSceneExcel().getPlayers().keySet().forEach(playerId -> {
            ChannelHandlerContext ctx = PlayerMap.getCtxId(playerId);
            ctx.writeAndFlush(notify);
        });

    }

    public void notifyPlayer(PlayerModel playerModel, StringBuilder sb) {
        playerModel.getChannelHandlerContext().writeAndFlush(sb);

    }

}
