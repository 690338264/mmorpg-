package com.function.user.map;

import com.function.player.model.Player;
import com.function.user.model.User;
import com.jpa.dao.UserDAO;
import com.jpa.entity.TPlayerInfo;
import com.jpa.entity.TUser;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Catherine
 */
@Component
public class UserMap {
    @Autowired
    private UserDAO userDAO;
    private final Map<ChannelHandlerContext, User> userCtxMap = new HashMap<>();

    private final Map<Long, User> userMap = new HashMap<>();

    private final Map<Long, Player> players = new HashMap<>();

    private final Map<Long, Map<Long, TPlayerInfo>> userPlayerMap = new HashMap<>();

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

    public Map<Long, User> getUserMap() {
        return userMap;
    }

    public void remove(ChannelHandlerContext ctx) {
        userCtxMap.remove(ctx);
    }

    public Map<Long, Player> getPlayers() {
        return players;
    }

    @PostConstruct
    public void initUserMap() {
        List<TUser> users = userDAO.findAll();
        users.forEach(tUser -> {
            User user = new User();
            BeanUtils.copyProperties(tUser, user);
            userMap.put(tUser.getId(), user);
        });
    }
}
