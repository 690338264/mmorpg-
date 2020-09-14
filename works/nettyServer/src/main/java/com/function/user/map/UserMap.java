package com.function.user.map;

import com.function.player.model.Player;
import com.function.user.model.User;
import com.jpa.entity.TPlayerInfo;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Catherine
 */
@Component
public class UserMap {
    private final Map<ChannelHandlerContext, User> userCtxMap = new HashMap<>();

    private final Map<Long, User> userMap = new HashMap<>();

    private final Map<Long, Player> players = new HashMap<>();

    private final Map<Long, Map<Long, TPlayerInfo>> userPlayerMap = new HashMap<>();

    public void putUserMap(Long userId, User user) {
        userMap.put(userId, user);
    }

    public User getUserById(Long userId) {
        return userMap.get(userId);
    }

    public void putUserPlayerMap(Long userId, Map<Long, TPlayerInfo> map) {
        userPlayerMap.put(userId, map);
    }

    public Player getPlayers(Long playerId) {
        return players.get(playerId);
    }

    public Map<Long, TPlayerInfo> getUserPlayerMap(Long userId) {
        return userPlayerMap.get(userId);
    }

    public void putUserctx(ChannelHandlerContext ctx, User user) {
        userCtxMap.put(ctx, user);
    }

    public User getUserctx(ChannelHandlerContext ctx) {
        return userCtxMap.get(ctx);
    }

    public void remove(ChannelHandlerContext ctx) {
        userCtxMap.remove(ctx);
    }

    public Map<Long, Player> getPlayers() {
        return players;
    }
}
