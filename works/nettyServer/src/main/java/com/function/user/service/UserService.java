package com.function.user.service;

import com.function.player.model.Player;
import com.function.player.model.SceneObjectTask;
import com.function.player.service.PlayerData;
import com.function.scene.manager.SceneManager;
import com.function.scene.model.Scene;
import com.function.scene.model.SceneObjectType;
import com.function.scene.model.SceneType;
import com.function.scene.service.NotifyScene;
import com.function.team.service.TeamService;
import com.function.user.map.PlayerMap;
import com.function.user.map.UserMap;
import com.function.user.model.User;
import com.jpa.dao.PlayerDAO;
import com.jpa.dao.UserDAO;
import com.jpa.entity.TPlayer;
import com.jpa.entity.TUser;
import com.manager.ThreadPoolManager;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

/**
 * @author Catherine
 */
@Slf4j
@Component
@SuppressWarnings("rawtypes")
public class UserService {

    @Autowired
    private PlayerData playerData;
    @Autowired
    private UserDAO usersDAO;
    @Autowired
    private PlayerDAO playerDAO;
    @Autowired
    private UserMap userMap;
    @Autowired
    private PlayerMap playerMap;
    @Autowired
    private NotifyScene notifyScene;
    @Autowired
    private SceneManager sceneManager;
    @Autowired
    private TeamService teamService;

    public static int mpAdd = 5;

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
        usersDAO.save(u);
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
            User user = userMap.getUserById(userId);
            if (user.getPsw().equals(psw)) {
                userMap.putUserctx(ctx, user);
                return true;
            } else {
                return false;
            }
        }
        User user = new User();
        TUser logUser = usersDAO.findByIdAndPsw(userId, psw);
        if (logUser == null) {
            return false;
        }
        BeanUtils.copyProperties(logUser, user);

        //查询用户角色
        Map<Long, TPlayer> playerMap = new HashMap<>();
        playerDAO.findByUserId(userId).forEach(player ->
                playerMap.put(player.getRoleId(), player));
        userMap.putUserPlayerMap(userId, playerMap);

        userMap.putUserctx(ctx, user);
        userMap.putUserMap(userId, user);
        return true;
    }

    /**
     * 查看角色列表
     */
    public Map<Long, TPlayer> listPlayer(Long id) {
        return userMap.getUserPlayerMap(id);
    }

    /**
     * 选择角色进行登录
     */
    public void logPlayer(Long playerId, ChannelHandlerContext ctx) {
        User user = getUserByCtx(ctx);

        TPlayer tplayer = userMap.getUserPlayerMap(user.getId()).get(playerId);
        if (tplayer == null) {
            ctx.writeAndFlush("该角色不存在！\n");
            return;
        }
        Scene scene = sceneManager.get(SceneType.PUBLIC.getType()).get(tplayer.getLoc());
        Player player = getPlayer(tplayer, playerId, scene);
        scene.getSceneObjectMap().get(SceneObjectType.PLAYER.getType()).put(playerId, player);

        player.setChannelHandlerContext(ctx);
        playerMap.putPlayerCtx(ctx, player);
        playerMap.getOfflinePlayer().remove(playerId);
        mpResume(player);
        //通知场景
        notifyScene.notifyScene(scene, MessageFormat.format("玩家[{0}]进入场景\n", player.getTPlayer().getName()));

    }

    /**
     * mp定时器开启
     */
    public void mpResume(Player player) {
        if (player.getTaskMap().get(SceneObjectTask.MP_ADD) != null) {
            return;
        }
        ScheduledFuture s = ThreadPoolManager.loopThread(() -> {
            if (player.getChannelHandlerContext() == null) {
                player.getTaskMap().get(SceneObjectTask.MP_ADD).cancel(true);
                player.getTaskMap().remove(SceneObjectTask.MP_ADD);
                return;
            }
            int nowMp = player.getMp() + mpAdd < player.getOriMp() ? player.getMp() + mpAdd : player.getOriMp();
            player.setMp(nowMp);
        }, 0, 10000, player.getChannelHandlerContext().hashCode());
        player.getTaskMap().put(SceneObjectTask.MP_ADD, s);
    }

    /**
     * 退出登录
     */
    public void logout(Player player) {
        ChannelHandlerContext ctx = player.getChannelHandlerContext();
        playerData.updatePlayer(player);
        teamService.leaveTeam(player);
        playerMap.remove(ctx, player.getTPlayer().getRoleId());
        userMap.remove(ctx);
        Scene scene = sceneManager.get(SceneType.PUBLIC.getType()).get(player.getNowScene().getId());
        scene.getSceneObjectMap().get(SceneObjectType.PLAYER.getType()).remove(player.getTPlayer().getRoleId());
        player.setChannelHandlerContext(null);
        playerMap.getOfflinePlayer().put(player.getTPlayer().getRoleId(), System.currentTimeMillis());
    }

    public Player getPlayer(TPlayer tPlayer, long playerId, Scene scene) {
        if (userMap.getPlayers(playerId) == null) {
            Player player = new Player();
            player.setTPlayer(tPlayer);
            player.setNowScene(scene);
            playerData.initPlayer(player);
            userMap.getPlayers().put(playerId, player);
            return player;
        } else {
            return userMap.getPlayers(playerId);
        }
    }

    public User getUserByCtx(ChannelHandlerContext ctx) {
        return userMap.getUserctx(ctx);
    }

    public Player getPlayerByCtx(ChannelHandlerContext ctx) {
        return playerMap.getPlayerCtx(ctx);
    }


}
