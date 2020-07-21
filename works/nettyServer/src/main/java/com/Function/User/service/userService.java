package com.Function.User.service;

import com.database.mapper.UserMap;
import com.database.entity.user;
import io.netty.channel.ChannelHandlerContext;
import org.apache.xmlbeans.impl.piccolo.util.DuplicateKeyException;

public class userService {
    public void register(ChannelHandlerContext ctx,String userName,String psw){
        user u = new user();
        u.setName(userName);
        u.setPwd(psw);
        try {

        }catch (DuplicateKeyException e){
            ctx.writeAndFlush("用户名已存在");
            return;
        }
    }

    public void login(long userId,String psw,ChannelHandlerContext ctx){

    }
}
