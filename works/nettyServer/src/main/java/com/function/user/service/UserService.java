package com.function.user.service;

import com.function.player.model.Player;
import com.function.player.service.PlayerData;
import com.function.player.service.PlayerService;
import com.function.scene.model.Scene;
import com.function.user.map.PlayerMap;
import com.function.user.map.UserMap;
import com.function.user.model.User;
import com.jpa.dao.PlayerDAO;
import com.jpa.dao.UserDAO;
import com.jpa.entity.TPlayer;
import com.jpa.entity.TUser;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Catherine
 */
@Slf4j
@Component

public class UserService {

    @Autowired
    private PlayerMap playerMap;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private PlayerData playerData;
    @Autowired
    private UserDAO usersDAO;
    @Autowired
    private PlayerDAO playerDAO;

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
    public void login(long userId, String psw, ChannelHandlerContext ctx) {
        User usermodel = new User();
        TUser logUser = usersDAO.findByIdAndPsw(userId, psw);
        if (logUser != null) {
            ctx.writeAndFlush("正在登陆......\n");
        } else {
            ctx.writeAndFlush("请输入正确的用户名和密码！\n");
            return;
        }
        BeanUtils.copyProperties(logUser, usermodel);
        UserMap.putUserctx(ctx, usermodel);
        usermodel.setChannelHandlerContext(ctx);
    }

    /**
     * 查看角色列表
     */
    public List<TPlayer> listPlayer(Long id) {
        List<TPlayer> list = playerDAO.findByUserId(id);
        log.info("角色列表：" + list);
        return list;
    }

    /**
     * 查询是否拥有该角色
     */
    public boolean hasPlayer(Long playerId, ChannelHandlerContext ctx) {
        User user = getUserByCtx(ctx);
        TPlayer player = playerDAO.findByRoleIdAndUserId(playerId, user.getId());
        return player != null;
    }

    /**
     * 选择角色进行登录
     */
    public Player logPlayer(Long playerId, ChannelHandlerContext ctx) {
        User user = getUserByCtx(ctx);
        TPlayer tPlayer = playerDAO.findByRoleIdAndUserId(playerId, user.getId());
        Player player = new Player();
        BeanUtils.copyProperties(tPlayer, player);

        PlayerMap.putPlayerCtx(ctx, player);
        PlayerMap.putCtxId(playerId, ctx);

        player.setChannelHandlerContext(ctx);
        Scene scene = new Scene();
        scene.setSceneId(player.getLoc());
        player.setNowScene(scene);
        scene.getSceneExcel().getPlayers().put(player.getRoleId(), player);
        playerData.initPlayer(player);
        return player;
    }

    public User getUserByCtx(ChannelHandlerContext ctx) {
        return UserMap.getUserctx(ctx);
    }

    public Player getPlayerByCtx(ChannelHandlerContext ctx) {
        return PlayerMap.getPlayerCtx(ctx);
    }


}
