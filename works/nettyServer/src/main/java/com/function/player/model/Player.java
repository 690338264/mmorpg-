package com.function.player.model;

import com.function.bag.model.Bag;
import com.function.email.model.Email;
import com.function.item.model.Item;
import com.function.scene.model.Instance;
import com.function.scene.model.Scene;
import com.function.scene.model.SceneObject;
import com.function.trade.model.TradeBoard;
import com.jpa.entity.TPlayer;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.util.*;


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
    private Scene nowScene;
    /**
     * 玩家所在副本
     */
    private Instance Instance;
    /**
     * 玩家背包
     */
    private Bag bag;

    private Long teamId;
    /**
     * 玩家当前mp
     */
    private Integer mp;
    /**
     * 玩家初始mp
     */
    private Integer oriMp;

    private Integer def;
    private Integer speed;
    private int levelUp = 2000;
    /**
     * 是否已加载好角色
     */
    private boolean init;

    private TradeBoard tradeBoard;

    private Map<Integer, Item> equipMap = new HashMap<>();

    private List<Email> emails = Collections.synchronizedList(new ArrayList<>());

    @Override
    public String getName() {
        return tPlayer.getName();
    }

    @Override
    public int getSceneId() {
        return nowScene.getSceneId();
    }

}
