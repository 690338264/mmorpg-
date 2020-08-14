package com.function.user.service;

import com.function.player.model.Player;
import com.function.player.service.PlayerData;
import com.function.player.service.PlayerService;
import com.function.scene.manager.SceneCache;
import com.function.scene.model.Scene;
import com.function.scene.service.NotifyScene;
import com.function.user.map.PlayerMap;
import com.function.user.map.UserMap;
import com.function.user.model.User;
import com.jpa.dao.PlayerDAO;
import com.jpa.dao.UserDAO;
import com.jpa.entity.TUser;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Catherine
 */
@Slf4j
@Component

public class UserService {

    @Autowired
    private PlayerService playerService;
    @Autowired
    private PlayerData playerData;
    @Autowired
    private UserDAO usersDAO;
    @Autowired
    private PlayerDAO playerDAO;
    @Autowired
    private SceneCache sceneCache;
    @Autowired
    private UserMap userMap;
    @Autowired
    private PlayerMap playerMap;
    @Autowired
    private NotifyScene notifyScene;

    /**
     * 用户注册
     */
    public void register(ChannelHandlerContext ctx, String userName, String psw) {
        TUser u = new TUser();
        u.setName(userName);
        u.setPsw(psw);
        if (usersDAO.findByName(userName) != null) {
            ctx.writeAndFlush("用户名已存在\n");
            return;
        }
        TUser newUser = usersDAO.findByName(userName);
        if (newUser != null) {
            ctx.writeAndFlush("注册成功,您的id为" + newUser.getId() + "用户名为" + newUser.getName() + '\n');
        } else {
            ctx.writeAndFlush("注册失败请重试!\n");
        }
    }

    /**
     * 用户登录
     */
    public boolean login(long userId, String psw, ChannelHandlerContext ctx) {
        if (userMap.getUserById(userId) != null) {
            userMap.putUserctx(ctx, userMap.getUserById(userId));
            return true;
        }
        User user = new User();
        TUser logUser = usersDAO.findByIdAndPsw(userId, psw);
        if (logUser == null) {
            return false;
        }
        BeanUtils.copyProperties(logUser, user);

        //查询用户角色
        Map<Long, Player> playerMap = new HashMap<>();
        playerDAO.findByUserId(userId).forEach(player -> {
            Player p = new Player();
            p.setTPlayer(player);
            p.setInit(false);
            playerMap.put(player.getRoleId(), p);
        });
        userMap.putPlayerMap(userId, playerMap);

        userMap.putUserctx(ctx, user);
        userMap.putUserMap(userId, user);
        return true;
    }

    /**
     * 查看角色列表
     */
    public Map<Long, Player> listPlayer(Long id) {
        return userMap.getPlayerMap(id);
    }

    /**
     * 选择角色进行登录
     */
    public void logPlayer(Long playerId, ChannelHandlerContext ctx) {
        User user = getUserByCtx(ctx);
        Player player = userMap.getPlayerMap(user.getId()).get(playerId);
        if (player == null) {
            ctx.writeAndFlush("该角色不存在！\n");
            return;
        }
        Scene scene = sceneCache.get("Scene" + player.getTPlayer().getLoc());
        //加载角色信息
        if (!player.isInit()) {
            scene.getPlayerMap().put(playerId, player);
            sceneCache.set(scene);
            player.setNowScene(scene);
            playerData.initPlayer(player);
            player.setInit(true);

        }

        player.setChannelHandlerContext(ctx);
        playerMap.putPlayerCtx(ctx, player);
        //通知场景
        StringBuilder log = new StringBuilder();
        log.append("玩家[").append(player.getTPlayer().getName()).append("]进入场景\n");
        notifyScene.notifyScene(scene, log);

    }

    /**
     * 退出登录
     */
    public void logout(Player player) {
        ChannelHandlerContext ctx = player.getChannelHandlerContext();
        playerMap.remove(ctx, player.getTPlayer().getRoleId());
        userMap.remove(ctx);
        player.setChannelHandlerContext(null);
    }

    public User getUserByCtx(ChannelHandlerContext ctx) {
        return userMap.getUserctx(ctx);
    }

    public Player getPlayerByCtx(ChannelHandlerContext ctx) {
        return playerMap.getPlayerCtx(ctx);
    }


}
