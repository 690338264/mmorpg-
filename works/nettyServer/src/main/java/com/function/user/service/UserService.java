package com.function.user.service;

import com.function.player.manager.PlayerManager;
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
import com.jpa.entity.TPlayerInfo;
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
    @Autowired
    private PlayerManager playerManager;

    public static int mpAdd = 5;

    /**
     * 用户注册
     */
    public void register(ChannelHandlerContext ctx, String userName, String psw) {
        for (User user : userMap.getUserMap().values()) {
            if (user.getName().equals(userName)) {
                ctx.writeAndFlush("用户名已存在\n");
                return;
            }
        }
        TUser tUser = new TUser();
        synchronized (this) {
            for (User user : userMap.getUserMap().values()) {
                if (user.getName().equals(userName)) {
                    ctx.writeAndFlush("用户名已存在\n");
                    return;
                }
            }
            tUser.setName(userName);
            tUser.setPsw(psw);
            usersDAO.saveAndFlush(tUser);
            User user = new User();
            BeanUtils.copyProperties(tUser, user);
            userMap.getUserMap().put(tUser.getId(), user);
        }
        ctx.writeAndFlush("注册成功,您的id为" + tUser.getId() + "用户名为" + tUser.getName() + '\n');
    }

    /**
     * 用户登录
     */
    public boolean login(long userId, String psw, ChannelHandlerContext ctx) {
        User user = userMap.getUserById(userId);
        if (user == null || !user.getPsw().equals(psw)) {
            return false;
        }
        if (userMap.getUserPlayerMap(userId) == null) {
            Map<Long, TPlayerInfo> playerInfoMap = new HashMap<>();
            playerManager.getPlayerInfoMap().forEach((playerId, playerInfo) -> {
                if (playerInfo.gettPlayerInfo().getUserId() == userId) {
                    playerInfoMap.put(playerId, playerInfo.gettPlayerInfo());
                }
            });
            userMap.putUserPlayerMap(userId, playerInfoMap);
        }
        userMap.putUserctx(ctx, user);
        return true;
    }

    /**
     * 查看角色列表
     */
    public Map<Long, TPlayerInfo> listPlayer(Long id) {
        return userMap.getUserPlayerMap(id);
    }

    /**
     * 选择角色进行登录
     */
    public void logPlayer(Long playerId, ChannelHandlerContext ctx) {
        ThreadPoolManager.immediateThread(() -> {
            User user = getUserByCtx(ctx);
            if (!userMap.getUserPlayerMap(user.getId()).containsKey(playerId)) {
                ctx.writeAndFlush("该角色不存在！\n");
                return;
            }
            if (userMap.getPlayers().containsKey(playerId)) {
                Player player = userMap.getPlayers().get(playerId);
                if (player.getChannelHandlerContext() != null) {
                    player.getChannelHandlerContext().writeAndFlush("该账号已在别处登录\n");
                    logout(player);
                }
                if (!player.isInit()) {
                    init(player);
                    player.setInit(true);
                }
                playerMap.getPlayerLastUpdate().remove(player.getTPlayer().getRoleId());
                getIntoScene(player, ctx);
                return;
            }
            Player player = new Player();
            TPlayer tPlayer = playerDAO.findByRoleId(playerId);
            player.setTPlayer(tPlayer);
            init(player);
            player.setInit(true);
            getIntoScene(player, ctx);
        }, playerId.intValue());
    }

    /**
     * mp定时器开启
     */
    public void mpResume(Player player) {
        if (player.getTaskMap().get(SceneObjectTask.MP_ADD) != null) {
            return;
        }
        ScheduledFuture<?> s = ThreadPoolManager.loopThread(() -> {
            if (player.getChannelHandlerContext() == null) {
                player.getTaskMap().get(SceneObjectTask.MP_ADD).cancel(true);
                player.getTaskMap().remove(SceneObjectTask.MP_ADD);
                return;
            }
            int nowMp = Math.min(player.getMp() + mpAdd, player.getOriMp());
            player.setMp(nowMp);
        }, 0, 10000, player.getChannelHandlerContext().hashCode());
        player.getTaskMap().put(SceneObjectTask.MP_ADD, s);
    }

    /**
     * 退出登录
     */
    public void logout(Player player) {
        ChannelHandlerContext ctx = player.getChannelHandlerContext();
        if (player.getNowScene().getSceneObjectMap().get(SceneObjectType.SUMMON).containsKey(player.getId())) {
            player.getNowScene().getSceneObjectMap().get(SceneObjectType.SUMMON).get(player.getId()).onDie();
        }
        teamService.leaveTeam(player);
        playerMap.remove(ctx, player.getTPlayer().getRoleId());
        userMap.remove(ctx);
        Scene scene = sceneManager.get(SceneType.PUBLIC.getType()).get(player.getNowScene().getId());
        scene.getSceneObjectMap().get(SceneObjectType.PLAYER).remove(player.getTPlayer().getRoleId());
        player.setChannelHandlerContext(null);
        playerMap.getPlayerLastUpdate().put(player.getTPlayer().getRoleId(), System.currentTimeMillis());
    }

    public void init(Player player) {
        playerData.initPlayer(player);
        int sceneId = player.getTPlayer().getLoc();
        Scene scene = sceneManager.get(SceneType.PUBLIC.getType()).get(sceneId);
        player.setNowScene(scene);
    }

    public void getIntoScene(Player player, ChannelHandlerContext ctx) {
        long playerId = player.getTPlayer().getRoleId();
        userMap.getPlayers().put(playerId, player);
        player.setChannelHandlerContext(ctx);
        playerMap.putPlayerCtx(ctx, player);
        mpResume(player);
        //通知场景
        player.getNowScene().getSceneObjectMap().get(SceneObjectType.PLAYER).put(playerId, player);
        notifyScene.notifyScene(player.getNowScene(), MessageFormat.format("玩家[{0}]进入场景\n", player.getTPlayer().getName()));
    }

    public User getUserByCtx(ChannelHandlerContext ctx) {
        return userMap.getUserctx(ctx);
    }

    public Player getPlayerByCtx(ChannelHandlerContext ctx) {
        return playerMap.getPlayerCtx(ctx);
    }
}