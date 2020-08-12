package com.function.user.map;

import com.function.user.model.User;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Catherine
 */
@Component
public class UserMap {
    private static Map<ChannelHandlerContext, User> userModelMap = new HashMap<>();

    public static void putUserctx(ChannelHandlerContext ctx, User user) {
        userModelMap.put(ctx, user);
    }

    public static User getUserctx(ChannelHandlerContext ctx) {
        return userModelMap.get(ctx);
    }
}
