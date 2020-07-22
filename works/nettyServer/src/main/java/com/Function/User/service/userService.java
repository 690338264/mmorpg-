package com.Function.User.service;

import com.database.entity.User;
import com.database.entity.UserExample;
import com.database.mapper.UserMapper;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service

public class userService {
    @Resource
    private UserMapper userMapper;

    public void register(ChannelHandlerContext ctx,String userName,String psw){
        User u = new User();
        u.setName(userName);
        u.setPsw(psw);
        try {
            userMapper.insertSelective(u);
        }catch (DuplicateKeyException e){
            ctx.writeAndFlush("用户名已存在");
            return;
        }
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andNameEqualTo(userName);
        //userExample.or().andNameEqualTo(userName);
        List<User> newUserList = userMapper.selectByExample(userExample);
        if(newUserList.size()>0){//判断是否添加成功
            User newUser = newUserList.get(0);//获取第0位（自己）
            ctx.writeAndFlush("注册成功，您的id为"+newUser.getId()+"用户名为"+newUser.getName());
        }
        else{
            ctx.writeAndFlush("注册失败~请重试");
        }
    }

   public void login(long userId,String psw,ChannelHandlerContext ctx){

    }
}
