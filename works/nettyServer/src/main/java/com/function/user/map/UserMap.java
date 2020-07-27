package com.function.user.map;

import com.function.user.model.UserModel;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserMap {
    private static Map<ChannelHandlerContext, UserModel> userModelMap = new HashMap<ChannelHandlerContext, UserModel>();

    public static void putUserctx(ChannelHandlerContext ctx,UserModel userModel){
        userModelMap.put(ctx, userModel);
    }

    public static UserModel getUserctx(ChannelHandlerContext ctx){
        return userModelMap.get(ctx);
    }
}
