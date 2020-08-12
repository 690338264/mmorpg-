package com.function.player.model;

import com.function.bag.model.Bag;
import com.function.item.model.Item;
import com.function.scene.model.Scene;
import com.function.skill.model.Skill;
import com.jpa.entity.TPlayer;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Catherine
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(exclude = {"channelHandlerContext", ""})
@Slf4j
public class Player extends TPlayer {

    private ChannelHandlerContext channelHandlerContext;
    /**
     * 玩家当前场景
     */
    private Scene nowScene;

    private Integer level;
    private Map<Integer, Skill> skillMap = new HashMap<>();
    private Map<Integer, Item> equipMap = new HashMap<>();
    private Bag bag;
    /**
     * 玩家当前hp
     */
    private Integer hp;
    /**
     * 玩家初始hp
     */
    private Integer oriHp;
    /**
     * 玩家当前mp
     */
    private Integer mp;
    /**
     * 玩家初始mp
     */
    private Integer oriMp;
    private Integer atk;
    private Integer def;
    private Integer speed;

}
