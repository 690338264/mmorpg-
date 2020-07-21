package com.Function.Player.model;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import com.model.NPC;

@Data
//@EqualsAndHashCode(callSuper = true)
@ToString(exclude = {"ctx",""})
@Slf4j
public class Player {
    private ChannelHandlerContext ctx;//当前通道上下文
    private NPC npc;




}
