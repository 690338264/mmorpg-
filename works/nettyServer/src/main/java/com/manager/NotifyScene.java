package com.manager;

import com.function.scene.model.SceneExcel;
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

    public void notifyScene(SceneExcel sceneExcel, String notify) {
        sceneExcel.getPlayers().keySet().forEach(playerId -> {
            ChannelHandlerContext ctx = PlayerMap.getCtxId(playerId);
            ctx.writeAndFlush(notify);
        });

    }

}
