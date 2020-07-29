package com.function.player.model;

import com.database.entity.Player;
import com.function.scene.model.Scene;
import com.function.skill.model.Skill;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(exclude = {"channelHandlerContext", ""})
@Slf4j
public class PlayerModel extends Player {
    private ChannelHandlerContext channelHandlerContext;
    private Scene nowScene;
    private Integer level;
    private Map<Integer, Skill> skillMap = new HashMap<Integer, Skill>();
    private Integer hp;
    private Integer mp;
    private Integer atk;
    private Integer def;
    private Integer speed;

}
