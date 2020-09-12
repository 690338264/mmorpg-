package com.function.player.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.event.EventHandler;
import com.event.EventManager;
import com.event.QuestEvent;
import com.function.bag.model.Bag;
import com.function.email.model.Email;
import com.function.item.model.Item;
import com.function.quest.model.Quest;
import com.function.quest.model.QuestState;
import com.function.quest.model.QuestType;
import com.function.scene.model.Dungeon;
import com.function.scene.model.Scene;
import com.function.scene.model.SceneObject;
import com.function.trade.model.TradeBoard;
import com.jpa.entity.TPlayer;
import com.jpa.entity.TPlayerInfo;
import com.manager.ThreadPoolManager;
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
//@Component
@EqualsAndHashCode(callSuper = true)
public class Player extends SceneObject {
    //        @Autowired
//    private EventManager eventManager;
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

    private TradeBoard tradeBoard;

    private Map<Integer, Item> equipMap = new HashMap<>();

    private List<Email> emails = Collections.synchronizedList(new ArrayList<>());

    private Map<Long, TPlayerInfo> friend = new HashMap<>();

    private Map<Long, TPlayerInfo> friendRequest = new ConcurrentHashMap<>();

    private Map<QuestType, Map<Integer, Quest>> onDoingQuest = new ConcurrentHashMap<>();

    private Map<QuestState, Map<QuestType, Map<Integer, Quest>>> questMap = new ConcurrentHashMap<>();

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
        tPlayer.setQuest(JSON.toJSONString(questMap));
    }

    public <E extends QuestEvent> void submitEvent(E event) {
        Player player = this;
        List<EventHandler> handlerList = EventManager.getEventList(event);
        handlerList.forEach((eventHandler
                -> ThreadPoolManager.immediateThread(()
                -> eventHandler.handle(event, player), tPlayer.getRoleId().intValue())));
    }
}
