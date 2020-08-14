package com.function.user.controller;

import com.Cmd;
import com.function.player.model.Player;
import com.function.user.model.User;
import com.function.user.service.UserService;
import com.handler.ControllerManager;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import util.Msg;
import util.ParamNumCheck;

import java.util.Map;

/**
 * @author Catherine
 */
@Service
@Slf4j
public class UserController {
    @Autowired
    private UserService userservice;

    {
        ControllerManager.add(Cmd.USER_CREATE, this::userCreate);
        ControllerManager.add(Cmd.USER_LOGIN, this::userLogin);
        ControllerManager.add(Cmd.USER_LISTPLAYER, this::playerList);
        ControllerManager.add(Cmd.PLAYER_LOG, this::playerLogin);
        ControllerManager.add(Cmd.LOG_OUT, this::logout);
    }

    private void userCreate(ChannelHandlerContext ctx, Msg msg) {
        String[] params = ParamNumCheck.numCheck(ctx, msg, 3);
        String username = params[1];
        String psw = params[2];
        userservice.register(ctx, username, psw);
    }

    private void userLogin(ChannelHandlerContext ctx, Msg msg) {
        String[] params = ParamNumCheck.numCheck(ctx, msg, 3);
        long userId = Long.parseLong(params[1]);
        String psw = params[2];
        String say = userservice.login(userId, psw, ctx) ? "正在登陆......\n" : "请输入正确的用户名和密码！\n";
        ctx.writeAndFlush(say);
    }

    private void playerList(ChannelHandlerContext ctx, Msg msg) {
        User user = userservice.getUserByCtx(ctx);
        Map<Long, Player> playerMap = userservice.listPlayer(user.getId());
        if (playerMap.size() == 0) {
            ctx.writeAndFlush("请先创建角色\n");
        } else {
            for (Long playerId : playerMap.keySet()) {
                ctx.writeAndFlush("角色id：" + playerId + '\n');
                ctx.writeAndFlush("角色名称：" + playerMap.get(playerId).getTPlayer().getName() + '\n');
            }
            ctx.writeAndFlush("---请选择您要登陆的角色---" + '\n');
        }
    }

    private void playerLogin(ChannelHandlerContext ctx, Msg msg) {
        String[] params = ParamNumCheck.numCheck(ctx, msg, 2);
        Long roleId = Long.valueOf(params[1]);
        userservice.logPlayer(roleId, ctx);
    }

    private void logout(ChannelHandlerContext ctx, Msg msg) {
        Player player = userservice.getPlayerByCtx(ctx);
        userservice.logout(player);
    }
}
