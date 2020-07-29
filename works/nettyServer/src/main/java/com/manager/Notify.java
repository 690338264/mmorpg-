package com.manager;

import com.function.scene.model.Scene;
import com.function.user.map.PlayerMap;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Notify {
    @Autowired
    private PlayerMap playerMap;

    public void notifyScene(Scene scene,String notify){
        scene.getPlayers().keySet().forEach(playerId->{
            ChannelHandlerContext ctx = PlayerMap.getCtxId(playerId);
            ctx.writeAndFlush(notify);
        });

    }

}
