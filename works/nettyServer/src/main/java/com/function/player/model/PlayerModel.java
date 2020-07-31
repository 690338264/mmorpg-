package com.function.player.model;

import com.database.entity.Player;
import com.function.scene.excel.SceneExcel;
import com.function.skill.excel.SkillExcel;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(exclude = {"channelHandlerContext", ""})
@Slf4j
public class PlayerModel extends Player {
    private ChannelHandlerContext channelHandlerContext;
    private SceneExcel nowScene;
    private Integer level;
    private Map<Integer, SkillExcel> skillMap = new HashMap<Integer, SkillExcel>();
    private Integer hp;
    private Integer mp;
    private Integer atk;
    private Integer def;
    private Integer speed;

}
