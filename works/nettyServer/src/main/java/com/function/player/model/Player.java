package com.function.player.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.function.bag.model.Bag;
import com.function.email.model.Email;
import com.function.item.model.Item;
import com.function.scene.model.Dungeon;
import com.function.scene.model.Scene;
import com.function.scene.model.SceneObject;
import com.function.trade.model.TradeBoard;
import com.jpa.entity.TPlayer;
import com.jpa.entity.TPlayerInfo;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author Catherine
 */

@Data
@Slf4j
@EqualsAndHashCode(callSuper = true)
public class Player extends SceneObject {
    private TPlayer tPlayer;
    private ChannelHandlerContext channelHandlerContext;
    /**
     * 玩家当前场景
     */
    @JSONField(serialize = false)
    private Scene nowScene;
    /**
     * 玩家所在副本
     */
    @JSONField(serialize = false)
    private Dungeon dungeon;
    /**
     * 玩家背包
     */
    private Bag bag;

    private Long teamId;
    /**
     * 玩家当前mp
     */
    private int mp;
    /**
     * 玩家初始mp
     */
    private int oriMp;

    private int def;
    private int speed;
    private int levelUp = 2000;

    @JSONField(serialize = false)
    private TradeBoard tradeBoard;

    @JSONField(serialize = false)
    private Map<Integer, Item> equipMap = new HashMap<>();

    @JSONField(serialize = false)
    private List<Email> emails = Collections.synchronizedList(new ArrayList<>());

    @JSONField(serialize = false)
    private Map<Long, TPlayerInfo> friend = new HashMap<>();

    @JSONField(serialize = false)
    private Map<Long, TPlayerInfo> friendRequest = new ConcurrentHashMap<>();

    @Override
    public String getName() {
        return tPlayer.getName();
    }

    @Override
    public int getSceneId() {
        return nowScene.getSceneId();
    }

    public void toJson() {
        String json = JSON.toJSONString(equipMap);
        tPlayer.setEquip(json);
        tPlayer.setFriend(JSON.toJSONString(friend));
        tPlayer.setFriendRequest(JSON.toJSONString(friendRequest));
    }

}
