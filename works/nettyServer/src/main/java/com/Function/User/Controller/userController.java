package com.Function.User.Controller;

import com.Cmd;
import com.Function.Player.model.playerModel;
import com.Function.User.service.userService;
import com.database.entity.Player;
import com.database.entity.User;
import com.handler.controllerManager;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import util.Msg;
import util.ParamNumCheck;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Component
public class userController {

    private static long userId;
    public static Player p;
    @Resource
    private userService userservice;
    {
        controllerManager.add(Cmd.USER_CREATE,this::userCreate);
        controllerManager.add(Cmd.USER_LOGIN,this::userLogin);
        controllerManager.add(Cmd.USER_LISTPLAYER,this::playerList);
        controllerManager.add(Cmd.PLAYER_LOG,this::playerLogin);
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
        playerModel playermodel = new playerModel();
        String[] params = ParamNumCheck.numCheck(ctx,msg,2);
        Long roleId = Long.valueOf(params[1]);
        Player player = userservice.logPlayer(roleId,userId);
        if(player.getRoleid()==9999L){
            ctx.writeAndFlush("您无该角色！请重新选择！");
        }
        else{
            p = player;
            BeanUtils.copyProperties(p,playermodel);
            ctx.writeAndFlush("进入游戏·····");
            playermodel.setChannelHandlerContext(ctx);
        }
    }
}
