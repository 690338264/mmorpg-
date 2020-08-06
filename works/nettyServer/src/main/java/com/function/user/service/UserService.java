package com.function.user.service;

import com.database.entity.Player;
import com.database.entity.PlayerExample;
import com.database.entity.User;
import com.database.entity.UserExample;
import com.database.mapper.PlayerMapper;
import com.database.mapper.UserMapper;
import com.function.player.model.PlayerModel;
import com.function.player.service.PlayerData;
import com.function.player.service.PlayerService;
import com.function.scene.model.Scene;
import com.function.user.map.PlayerMap;
import com.function.user.map.UserMap;
import com.function.user.model.UserModel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Catherine
 */
@Slf4j
@Component
public class UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PlayerMapper playerMapper;
    @Autowired
    private PlayerMap playerMap;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private PlayerData playerData;

    /**
     * 用户注册
     */
    public void register(ChannelHandlerContext ctx, String userName, String psw) {
        User u = new User();
        u.setName(userName);
        u.setPsw(psw);
        try {
            userMapper.insertSelective(u);
        } catch (DuplicateKeyException e) {
            ctx.writeAndFlush("用户名已存在\n");
            return;
        }
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andNameEqualTo(userName);
        List<User> newUserList = userMapper.selectByExample(userExample);
        if (newUserList.size() > 0) {
            User newUser = newUserList.get(0);
            ctx.writeAndFlush("注册成功，您的id为" + newUser.getId() + "用户名为" + newUser.getName() + '\n');
        } else {
            ctx.writeAndFlush("注册失败~请重试\n");
        }
    }

    /**
     * 用户登录
     */
    public void login(long userId, String psw, ChannelHandlerContext ctx) {
        UserModel usermodel = new UserModel();
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andIdEqualTo(userId);
        criteria.andPswEqualTo(psw);
        List<User> UserList = userMapper.selectByExample(userExample);
        if (UserList.size() > 0) {
            ctx.writeAndFlush("正在登陆......\n");
        } else {
            ctx.writeAndFlush("请输入正确的用户名和密码！\n");
            return;
        }
        BeanUtils.copyProperties(UserList.get(0), usermodel);
        UserMap.putUserctx(ctx, usermodel);
        usermodel.setChannelHandlerContext(ctx);
    }

    /**
     * 查看角色列表
     */
    public List<Player> listPlayer(Long Id) {
        PlayerExample playerExample = new PlayerExample();
        PlayerExample.Criteria criteria = playerExample.createCriteria();
        criteria.andIdEqualTo(Id);
        List<Player> PlayerList = playerMapper.selectByExample(playerExample);
        log.info("角色列表：" + PlayerList);
        return PlayerList;
    }

    /**
     * 查询是否拥有该角色
     */
    public boolean hasPlayer(Long playerId, ChannelHandlerContext ctx) {
        UserModel userModel = getUserByCtx(ctx);
        PlayerExample playerExample = new PlayerExample();
        PlayerExample.Criteria criteria = playerExample.createCriteria();
        criteria.andRoleidEqualTo(playerId);
        criteria.andIdEqualTo(userModel.getId());
        List<Player> players = playerMapper.selectByExample(playerExample);
        if (players.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 选择角色进行登录
     */
    public PlayerModel logPlayer(Long playerId, ChannelHandlerContext ctx) {
        UserModel userModel = getUserByCtx(ctx);
        PlayerExample playerExample = new PlayerExample();
        PlayerExample.Criteria criteria = playerExample.createCriteria();
        criteria.andRoleidEqualTo(playerId);
        criteria.andIdEqualTo(userModel.getId());
        List<Player> playerList = playerMapper.selectByExample(playerExample);
        PlayerModel playerModel = new PlayerModel();
        BeanUtils.copyProperties(playerList.get(0), playerModel);

        PlayerMap.putPlayerCtx(ctx, playerModel);
        PlayerMap.putCtxId(playerId, ctx);

        playerModel.setChannelHandlerContext(ctx);
        Scene scene = new Scene();
        scene.setSceneId(playerModel.getLoc());
        playerModel.setNowScene(scene);
        scene.getSceneExcel().getPlayers().put(playerModel.getRoleid(), playerModel);
        playerData.initPlayer(playerModel);
        return playerModel;
    }

    public UserModel getUserByCtx(ChannelHandlerContext ctx) {
        return UserMap.getUserctx(ctx);
    }

    public PlayerModel getPlayerByCtx(ChannelHandlerContext ctx) {
        return PlayerMap.getPlayerCtx(ctx);
    }


}
