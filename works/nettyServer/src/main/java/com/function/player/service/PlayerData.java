package com.function.player.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.function.bag.model.Bag;
import com.function.email.model.Email;
import com.function.item.excel.ItemExcel;
import com.function.item.model.Item;
import com.function.occ.excel.OccExcel;
import com.function.occ.excel.OccResource;
import com.function.player.model.Player;
import com.function.player.model.PlayerInfo;
import com.function.player.model.SceneObjectTask;
import com.function.quest.excel.QuestResource;
import com.function.quest.model.Quest;
import com.function.quest.model.QuestState;
import com.function.quest.model.QuestTimes;
import com.function.quest.model.QuestType;
import com.function.scene.model.SceneObjectType;
import com.function.skill.model.Skill;
import com.jpa.dao.BagDAO;
import com.jpa.dao.EmailDAO;
import com.jpa.dao.PlayerDAO;
import com.jpa.dao.PlayerInfoDAO;
import com.jpa.entity.TBag;
import com.jpa.entity.TEmail;
import com.jpa.entity.TPlayer;
import com.jpa.entity.TPlayerInfo;
import com.jpa.manager.JpaManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.IntStream;

/**
 * @author Catherine
 * @create 2020-08-06 19:45
 */
@Component
@SuppressWarnings("rawtypes")
public class PlayerData {
    @Autowired
    private BagDAO bagDAO;
    @Autowired
    private PlayerDAO playerDAO;
    @Autowired
    private JpaManager jpaManager;
    @Autowired
    private EmailDAO emailDAO;
    @Autowired
    private PlayerInfoDAO playerInfoDAO;

    /**
     * 初始化角色属性
     */
    public void initAttribute(Player player) {
        OccExcel occ = OccResource.getOccById(player.getTPlayer().getOccupation());
        int level = player.getTPlayer().getLevel();

        player.setOriHp(occ.getHp() + level * occ.getHp() / occ.getMultiple());
        player.setOriMp(occ.getMp() + level * occ.getMp() / occ.getMultiple());
        player.setHp(player.getOriHp());
        player.setMp(player.getOriMp());

        player.setAtk(occ.getAtk() + level * occ.getAtk() / occ.getMultiple());
        player.setDef(occ.getDef() + level * occ.getDef() / occ.getMultiple());
        player.setSpeed(occ.getSpeed() + level * occ.getSpeed() / occ.getMultiple());
        for (Integer space : player.getEquipMap().keySet()) {
            ItemExcel item = player.getEquipMap().get(space).getItemById();
            int addAtk = item.getAtk();
            int addDef = item.getDef();
            int addSpeed = item.getSpeed();
            player.setAtk(player.getAtk() + addAtk);
            player.setDef(player.getDef() + addDef);
            player.setSpeed(player.getSpeed() + addSpeed);
        }

    }

    /**
     * 初始化角色技能
     */
    public void initSkill(Player player) {
        OccExcel occExcel = OccResource.getOccById(player.getTPlayer().getOccupation());
        IntStream.range(0, occExcel.getSkillId().size()).forEach(i -> {
            Skill skill = new Skill(occExcel.getSkillId().get(i));
            player.getCanUseSkill().put(i + 1, skill);
        });
    }

    /**
     * 初始化角色装备
     */
    public void initEquipment(Player player) {
        String json = player.getTPlayer().getEquip();
        Map<Integer, Item> m = JSON.parseObject(json, new TypeReference<Map<Integer, Item>>() {
        });
        player.setEquipMap(m);

    }

    /**
     * 初始化邮件
     */
    public void initEmail(Player player) {
        List<TEmail> tEmails = emailDAO.findByPlayerId(player.getTPlayer().getRoleId());
        tEmails.forEach(tEmail -> {
            Email email = new Email();
            email.settEmail(tEmail);
            String json = tEmail.getGift();
            List<Item> gifts = JSON.parseObject(json, new TypeReference<List<Item>>() {
            });
            email.setGifts(gifts);
            player.getEmails().add(email);
        });
    }

    /**
     * 初始化好友
     */
    public void initFriend(Player player) {
        player.setFriend(JSON.parseObject(player.getTPlayer().getFriend(), new TypeReference<Map<Long, TPlayerInfo>>() {
        }));
        player.setFriendRequest(JSON.parseObject(player.getTPlayer().getFriendRequest(), new TypeReference<Map<Long, TPlayerInfo>>() {
        }));
    }

    /**
     * 初始化玩家
     */
    public void initPlayer(Player player) {
        player.setType(SceneObjectType.PLAYER);
        initQuest(player);
        initSkill(player);
        initEquipment(player);
        initBag(player);
        initAttribute(player);
        initEmail(player);
        initFriend(player);
    }

    /**
     * 加载背包
     */
    public void initBag(Player player) {
        TBag tBag = bagDAO.findByPlayerId(player.getTPlayer().getRoleId());
        Bag bag = new Bag();
        bag.setTBag(tBag);
        player.setBag(bag);
        String json = tBag.getItem();
        Map<Integer, Item> m = JSON.parseObject(json, new TypeReference<Map<Integer, Item>>() {
        });
        bag.setItemMap(m);
    }

    public void initQuest(Player player) {
        TPlayer tPlayer = player.getTPlayer();
        if (tPlayer.getQuest() == null && tPlayer.getOnDoingQuest() == null) {
            createQuest(player);
            updatePlayer(player);
        } else {
            Map<QuestType, Map<Integer, Quest>> onDoingMap = JSON.parseObject(tPlayer.getOnDoingQuest(),
                    new TypeReference<Map<QuestType, Map<Integer, Quest>>>() {
                    });
            player.setOnDoingQuest(onDoingMap);
            Map<QuestState, List<Integer>> questMap = JSON.parseObject(tPlayer.getQuest(),
                    new TypeReference<Map<QuestState, List<Integer>>>() {
                    });
            player.setQuestMap(questMap);
        }
    }

    public void createQuest(Player player) {
        player.getQuestMap().computeIfAbsent(QuestState.CAN_BUT_NOT, key -> new CopyOnWriteArrayList<>());
        QuestResource.getQuestExcelMap().forEach((questId, questExcel) -> {
            if (questExcel.getTimes() == QuestTimes.ACHIEVEMENT.getType()) {
                Quest quest = new Quest(questId);
                QuestType questType = QuestType.values()[questExcel.getType() - 1];
                player.getOnDoingQuest().computeIfAbsent(questType, key -> new ConcurrentHashMap<>());
                player.getOnDoingQuest().get(questType).put(questId, quest);
            } else if (questExcel.getLevel() == 0) {
                player.getQuestMap().get(QuestState.CAN_BUT_NOT).add(questId);
            }
        });
    }

    /**
     * 存储玩家数据
     */
    public void updatePlayer(Player player) {
        ScheduledFuture update = jpaManager.update(player.getTaskMap().get(SceneObjectTask.UPDATE_PLAYER), () -> {
            player.toJson();
            playerDAO.save(player.getTPlayer());
            player.getTaskMap().remove(SceneObjectTask.UPDATE_PLAYER);
        }, player.getTPlayer().getRoleId().intValue());
        if (update != null) {
            player.getTaskMap().put(SceneObjectTask.UPDATE_PLAYER, update);
        }
    }

    public void updatePlayerInfo(PlayerInfo playerInfo) {
        ScheduledFuture update = jpaManager.update(playerInfo.getUpdate(), () -> {
            playerInfoDAO.save(playerInfo.gettPlayerInfo());
            playerInfo.setUpdate(null);
        }, playerInfo.gettPlayerInfo().getPlayerId().intValue());
        playerInfo.setUpdate(update);
    }
}
