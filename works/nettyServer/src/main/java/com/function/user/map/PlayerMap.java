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

    private final Map<ChannelHandlerContext, Player> playerCtxMap = new HashMap<>();

    private final Map<Long, ChannelHandlerContext> ctxPlayer = new HashMap<>();

    private final Map<Long, Long> offlinePlayer = new HashMap<>();

    public void putPlayerCtx(ChannelHandlerContext ctx, Player player) {
        playerCtxMap.put(ctx, player);
        ctxPlayer.put(player.getTPlayer().getRoleId(), ctx);
    }

    public Player getPlayerCtx(ChannelHandlerContext ctx) {
        return playerCtxMap.get(ctx);
    }

    public ChannelHandlerContext getCtxPlayer(Long playerId) {
        return ctxPlayer.get(playerId);
    }

    public Map<Long, Long> getOfflinePlayer() {
        return offlinePlayer;
    }

    public Map<ChannelHandlerContext, Player> getPlayerCtxMap() {
        return playerCtxMap;
    }

    public void remove(ChannelHandlerContext ctx, Long playerId) {
        playerCtxMap.remove(ctx);
        ctxPlayer.remove(playerId);
    }
}