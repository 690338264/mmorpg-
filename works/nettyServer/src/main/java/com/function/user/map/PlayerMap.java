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

    private Map<ChannelHandlerContext, Player> playerModelMap = new HashMap<>();

    private Map<Long, ChannelHandlerContext> ctxPlayer = new HashMap<>();

    public void putPlayerCtx(ChannelHandlerContext ctx, Player player) {
        playerModelMap.put(ctx, player);
        ctxPlayer.put(player.getTPlayer().getRoleId(), ctx);
    }

    public Player getPlayerCtx(ChannelHandlerContext ctx) {
        return playerModelMap.get(ctx);
    }

    public ChannelHandlerContext getCtxPlayer(Long playerId) {
        return ctxPlayer.get(playerId);
    }

    public void remove(ChannelHandlerContext ctx, Long playerId) {
        playerModelMap.remove(ctx);
        ctxPlayer.remove(playerId);
    }
}