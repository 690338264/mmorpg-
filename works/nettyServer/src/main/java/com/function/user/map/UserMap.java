package com.function.user.map;

import com.function.player.model.Player;
import com.function.user.model.User;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Catherine
 */
@Component
public class UserMap {
    private Map<ChannelHandlerContext, User> userCtxMap = new HashMap<>();

    private Map<Long, User> userMap = new HashMap<>();
    private Map<Long, Map<Long, Player>> playerMap = new HashMap<>();

    public void putUserMap(Long userId, User user) {
        userMap.put(userId, user);
    }

    public User getUserById(Long userId) {
        return userMap.get(userId);
    }

    public void putPlayerMap(Long userId, Map<Long, Player> map) {
        playerMap.put(userId, map);
    }

    public Map<Long, Player> getPlayerMap(Long userId) {
        return playerMap.get(userId);
    }

    public void putUserctx(ChannelHandlerContext ctx, User user) {
        userCtxMap.put(ctx, user);
    }

    public User getUserctx(ChannelHandlerContext ctx) {
        return userCtxMap.get(ctx);
    }
}
