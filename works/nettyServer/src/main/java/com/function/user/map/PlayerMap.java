package com.function.user.map;

import com.function.player.model.PlayerModel;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PlayerMap {
    private static Map<ChannelHandlerContext, PlayerModel> playerModelMap = new HashMap<ChannelHandlerContext, PlayerModel>();

    public static void putPlayerCtx(ChannelHandlerContext ctx,PlayerModel playerModel){
        playerModelMap.put(ctx,playerModel);
    }

    public static PlayerModel getPlayerCtx(ChannelHandlerContext ctx){
        return playerModelMap.get(ctx);
    }
}
