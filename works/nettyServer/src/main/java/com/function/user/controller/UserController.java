package com.function.user.controller;

import com.Cmd;
import com.function.player.model.Player;
import com.function.user.service.UserService;
import com.handler.ControllerManager;
import com.jpa.entity.TPlayer;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import util.Msg;
import util.ParamNumCheck;

import java.util.List;

/**
 * @author Catherine
 */
@Service
@Slf4j
public class UserController {

    private static long userId;
    @Autowired
    private UserService userservice;

    {
        ControllerManager.add(Cmd.USER_CREATE, this::userCreate);
        ControllerManager.add(Cmd.USER_LOGIN, this::userLogin);
        ControllerManager.add(Cmd.USER_LISTPLAYER, this::playerList);
        ControllerManager.add(Cmd.PLAYER_LOG, this::playerLogin);
    }

    private void userCreate(ChannelHandlerContext ctx, Msg msg) {
        String[] params = ParamNumCheck.numCheck(ctx, msg, 3);
        String username = params[1];
        String psw = params[2];
        userservice.register(ctx, username, psw);
    }

    private void userLogin(ChannelHandlerContext ctx, Msg msg) {
        String[] params = ParamNumCheck.numCheck(ctx, msg, 3);
        userId = Long.parseLong(params[1]);
        String psw = params[2];
        userservice.login(userId, psw, ctx);

    }

    private void playerList(ChannelHandlerContext ctx, Msg msg) {
        List<TPlayer> list = userservice.listPlayer(userId);
        if (list.size() == 0) {
            ctx.writeAndFlush("请先创建角色\n");
        } else {
            for (TPlayer tPlayer : list) {
                ctx.writeAndFlush("角色id：" + tPlayer.getRoleId() + '\n');
                ctx.writeAndFlush("角色名称：" + tPlayer.getName() + '\n');
            }
            ctx.writeAndFlush("---请选择您要登陆的角色---" + '\n');
        }
    }

    private void playerLogin(ChannelHandlerContext ctx, Msg msg) {
        String[] params = ParamNumCheck.numCheck(ctx, msg, 2);
        Long roleId = Long.valueOf(params[1]);
        if (userservice.hasPlayer(roleId, ctx)) {
            Player player = userservice.logPlayer(roleId, ctx);
            //获取场景
            ctx.writeAndFlush("[" + player.getName() + "]登录成功！\n当前所在位置为："
                    + player.getNowScene().getSceneExcel().getName() + "您的等级为：" + player.getLevel() + '\n');
        } else {
            ctx.writeAndFlush("无该角色！！！\n");
        }

    }

}
