package com.Function.User.Controller;

import com.Cmd;
import com.Function.User.service.userService;
import com.handler.controllerManager;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import util.Msg;
import util.ParamNumCheck;

import javax.annotation.Resource;

@Slf4j
@Component
public class userController {

    @Resource
    private userService userservice;
    {
        controllerManager.add(Cmd.USER_CREATE,this::userCreate);
        controllerManager.add(Cmd.USER_LOGIN,this::userLogin);
    }

    private void userCreate(ChannelHandlerContext ctx, Msg msg){
        String[] params = ParamNumCheck.numCheck(ctx,msg,3);
        String username = params[1];
        String psw = params[2];
        userservice.register(ctx,username,psw);
    }

    private void userLogin(ChannelHandlerContext ctx,Msg msg){
        String[] params = ParamNumCheck.numCheck(ctx,msg,3);
        long userId = Long.valueOf(params[1]);
        String psw = params[2];
        userservice.login(userId,psw,ctx);

    }

}
