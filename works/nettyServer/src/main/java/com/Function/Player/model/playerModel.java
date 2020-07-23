package com.Function.Player.model;

import com.database.entity.Player;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import com.model.NPC;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(exclude = {"channelHandlerContext",""})
@Slf4j
public class playerModel extends Player {
    private ChannelHandlerContext channelHandlerContext;//当前通道上下文
}
