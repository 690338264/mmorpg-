package com.function.user.controller;

import com.Cmd;
import com.function.player.model.PlayerModel;
import com.function.user.model.UserModel;
import com.function.user.service.UserService;
import com.database.entity.Player;
import com.handler.ControllerManager;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import util.Msg;
import util.ParamNumCheck;

import javax.annotation.Resource;
import java.util.List;
@Service
@Slf4j
@Component
public class UserController {

    private static long userId;
    private static PlayerModel playermodel;
    private static UserModel userModel;
    @Autowired
    private UserService userservice;

    {
        ControllerManager.add(Cmd.USER_CREATE,this::userCreate);
        ControllerManager.add(Cmd.USER_LOGIN,this::userLogin);
        ControllerManager.add(Cmd.USER_LISTPLAYER,this::playerList);
        ControllerManager.add(Cmd.PLAYER_LOG,this::playerLogin);
    }

    private void userCreate(ChannelHandlerContext ctx, Msg msg){
        String[] params = ParamNumCheck.numCheck(ctx,msg,3);
        String username = params[1];
        String psw = params[2];
        userservice.register(ctx,username,psw);
    }

    private void userLogin(ChannelHandlerContext ctx,Msg msg){
        String[] params = ParamNumCheck.numCheck(ctx,msg,3);
        userId = Long.valueOf(params[1]);
        String psw = params[2];
        userservice.login(userId,psw,ctx);

    }

    private void playerList(ChannelHandlerContext ctx,Msg msg){
        List< Player > list = userservice.listPlayer(userId);
        for(int i = 0;i<list.size();i++){
            ctx.writeAndFlush("角色id："+list.get(i).getRoleid()+'\n');
            ctx.writeAndFlush("角色名称："+list.get(i).getName()+'\n');
        }
        ctx.writeAndFlush("---请选择您要登陆的角色---"+'\n');
    }

    private void playerLogin(ChannelHandlerContext ctx,Msg msg){
        PlayerModel playermodel = new PlayerModel();
        String[] params = ParamNumCheck.numCheck(ctx,msg,2);
        Long roleId = Long.valueOf(params[1]);
        Player player = userservice.logPlayer(roleId,userId);
        if(player.getRoleid()==9999L){
            ctx.writeAndFlush("您无该角色！请重新选择！");
        }
        else{
            BeanUtils.copyProperties(player,playermodel);
            ctx.writeAndFlush("进入游戏·····");
            playermodel.setChannelHandlerContext(ctx);
        }
    }

    public static PlayerModel getPlayermodel() {
        return playermodel;
    }

    public static long getUserId() {
        return userId;
    }

    public static UserModel getUserModel() {
        return userModel;
    }
}
