package com.function.player.model;

import com.database.entity.Player;
import com.function.bag.model.BagModel;
import com.function.item.model.Item;
import com.function.scene.excel.SceneExcel;
import com.function.skill.model.Skill;
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
    private Map<Integer, Skill> skillMap = new HashMap<Integer, Skill>();
    private Map<Integer, Item> equipMap = new HashMap<Integer, Item>();
    private BagModel bagModel;
    private Integer hp;
    private Integer oriHp;
    private Integer mp;
    private Integer oriMp;
    private Integer atk;
    private Integer def;
    private Integer speed;

}
