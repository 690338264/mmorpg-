package com.function.scene.service;

import com.function.player.model.Player;
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
        scene.getPlayerMap().keySet().forEach(playerId -> {
            ChannelHandlerContext ctx = playerMap.getCtxPlayer(playerId);
            ctx.writeAndFlush(notify);
        });

    }

    public void notifyPlayer(Player player, StringBuilder sb) {
        player.getChannelHandlerContext().writeAndFlush(sb);

    }

}
