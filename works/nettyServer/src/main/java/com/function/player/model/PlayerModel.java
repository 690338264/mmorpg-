package com.function.player.model;

import com.database.entity.Player;
import com.function.scene.model.Scene;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(exclude = {"channelHandlerContext",""})
@Slf4j
public class PlayerModel extends Player {
    private ChannelHandlerContext channelHandlerContext;//当前通道上下文
    private Scene nowScene;
    private Integer level;

}
