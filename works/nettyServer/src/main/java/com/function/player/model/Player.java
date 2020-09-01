package com.function.player.model;

import com.function.bag.model.Bag;
import com.function.communicate.model.Email;
import com.function.item.model.Item;
import com.function.scene.model.Instance;
import com.function.scene.model.Scene;
import com.function.scene.model.SceneObject;
import com.jpa.entity.TPlayer;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Catherine
 */
@Data
@ToString(exclude = {"channelHandlerContext", ""})
@Slf4j
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

    private Map<Integer, Item> equipMap = new HashMap<>();
    private Map<Integer, Email> emailMap = new HashMap<>();

    @Override
    public String getName() {
        return tPlayer.getName();
    }
}
