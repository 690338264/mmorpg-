package com.function.player.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.event.EventHandler;
import com.event.EventManager;
import com.event.QuestEvent;
import com.function.bag.model.Bag;
import com.function.email.model.Email;
import com.function.item.model.Item;
import com.function.monster.model.Monster;
import com.function.quest.model.Quest;
import com.function.quest.model.QuestState;
import com.function.quest.model.QuestType;
import com.function.scene.model.*;
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
    private Dungeon dungeon;
    /**
     * 玩家背包
     */
    private Bag bag;

    private Long teamId;
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

    private Map<QuestState, List<Integer>> questMap = new ConcurrentHashMap<>();

    @Override
    public String getName() {
        return tPlayer.getName();
    }

    @Override
    public Long getId() {
        return tPlayer.getRoleId();
    }

    @Override
    public void onDie() {
        setState(SceneObjectState.DEATH);
        Map<Long, SceneObject> monsterMap = nowScene.getSceneObjectMap().get(SceneObjectType.MONSTER);
        monsterMap.forEach((monsterId, sceneObject) -> {
            Monster monster = (Monster) sceneObject;
            monster.getHurtList().remove(tPlayer.getRoleId());
        });
        ThreadPoolManager.delayThread(() -> {
            setHp(getOriHp());
            setState(SceneObjectState.NORMAL);
        }, 5000, tPlayer.getRoleId().intValue());
        getBuffs().forEach((k, v) -> {
            v.cancel(true);
            getBuffs().remove(k);
        });
    }

    @Override
    public Map<String, Object> getAttributeMap() {
        Map<String, Object> attributeMap = new HashMap<>();
        attributeMap.put("atk", getAtk());
        attributeMap.put("speed", speed);
        attributeMap.put("def", def);
        return attributeMap;
    }

    public void toJson() {
        if (equipMap != null) {
            tPlayer.setEquip(JSON.toJSONString(equipMap));
        }
        if (friend != null) {
            tPlayer.setFriend(JSON.toJSONString(friend));
        }
        if (friendRequest != null) {
            tPlayer.setFriendRequest(JSON.toJSONString(friendRequest));
        }
        if (questMap != null) {
            tPlayer.setQuest(JSON.toJSONString(questMap));
        }
        if (onDoingQuest != null) {
            tPlayer.setOnDoingQuest(JSON.toJSONString(onDoingQuest));
        }

    }

    public <E extends QuestEvent> void submitEvent(E event) {
        Player player = this;
        List<EventHandler> handlerList = EventManager.getEventList(event);
        handlerList.forEach((eventHandler
                -> ThreadPoolManager.immediateThread(()
                -> eventHandler.handle(event, player), tPlayer.getRoleId().intValue())));
    }
}
