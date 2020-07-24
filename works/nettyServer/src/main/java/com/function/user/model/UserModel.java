package com.function.user.model;

import com.database.entity.User;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper=true)
public class UserModel extends User {
    private ChannelHandlerContext channelHandlerContext;

}
