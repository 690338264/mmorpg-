package com.function.user.model;

import com.jpa.entity.TUser;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Catherine
 */
@Data
@EqualsAndHashCode(callSuper = true)

public class User extends TUser {

    private ChannelHandlerContext channelHandlerContext;

}
