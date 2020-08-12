package com.function.user.map;

import com.function.player.model.Player;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Catherine
 */
@Component
public class PlayerMap {
    private static Map<ChannelHandlerContext, Player> playerModelMap = new HashMap<>();
    private static Map<Long, ChannelHandlerContext> ctxIdMap = new HashMap<>();

    public static void putPlayerCtx(ChannelHandlerContext ctx, Player player) {
        playerModelMap.put(ctx, player);
    }

    public static Player getPlayerCtx(ChannelHandlerContext ctx) {
        return playerModelMap.get(ctx);
    }

    public static void putCtxId(Long playerId, ChannelHandlerContext ctx) {
        ctxIdMap.put(playerId, ctx);
    }

    public static ChannelHandlerContext getCtxId(Long playerId) {
        return ctxIdMap.get(playerId);
    }

}
