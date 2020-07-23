package com.Function.User.model;

import com.database.entity.User;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper=true)
public class userModel extends User {
    private ChannelHandlerContext channelHandlerContext;

}
