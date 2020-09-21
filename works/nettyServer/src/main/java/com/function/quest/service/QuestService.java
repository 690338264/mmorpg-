package com.function.quest.service;

import com.function.email.model.Email;
import com.function.email.service.EmailService;
import com.function.item.excel.ItemResource;
import com.function.item.model.Item;
import com.function.item.service.ItemService;
import com.function.player.model.Player;
import com.function.player.service.PlayerData;
import com.function.player.service.PlayerService;
import com.function.quest.excel.QuestExcel;
import com.function.quest.excel.QuestResource;
import com.function.quest.model.Quest;
import com.function.quest.model.QuestState;
import com.function.quest.model.QuestTimes;
import com.function.quest.model.QuestType;
import com.function.scene.service.NotifyScene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

/**
 * @author Catherine
 * @create 2020-09-12 15:05
 */
@Component
public class QuestService {
    @Autowired
    private NotifyScene notifyScene;
    @Autowired
    private PlayerData playerData;
    @Autowired
    private ItemService itemService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private PlayerService playerService;

    /**
     * 查看任务
     */
    public void showQuest(Player player) {
        StringBuilder context = new StringBuilder("正在进行中的任务\n");
        player.getOnDoingQuest().forEach(((questType, questMap)
                -> questMap.forEach((questId, quest)
                -> {
            context.append(MessageFormat.format("[{0}]{1}：{2}\n",
                    questId, quest.getQuestById().getName(), quest.getQuestById().getText()));
            IntStream.range(0, quest.getCfgList().size()).forEach((index)
                    -> context.append(MessageFormat.format("进度{0}/{1}\n",
                    quest.getProgress().get(index), quest.getCfgList().get(index).getValue())));
        })));
        context.append("待领取奖励的任务:\n");
        player.getQuestMap().computeIfAbsent(QuestState.COMPLETE, key -> new CopyOnWriteArrayList<>());
        player.getQuestMap().get(QuestState.COMPLETE).forEach((questId) -> {
            QuestExcel questExcel = QuestResource.getQuestById(questId);
            context.append(MessageFormat.format("[{0}]:{1} 奖励:{2}  金币:{3}\n",
                    questId, questExcel.getName(), ItemResource.getItemById(questExcel.getItem()).getName(), questExcel.getMoney()));
        });
        context.append("可领取的任务:\n");
        player.getQuestMap().get(QuestState.CAN_BUT_NOT).forEach((questId) -> {
            QuestExcel questExcel = QuestResource.getQuestById(questId);
            context.append(MessageFormat.format("[{0}]:{1}\n", questId, questExcel.getName()));
        });
        notifyScene.notifyPlayer(player, context);
    }

    /**
     * 接受任务
     */
    public void acceptQuest(Player player, Integer questId) {
        if (!player.getQuestMap().get(QuestState.CAN_BUT_NOT).contains(questId)) {
            notifyScene.notifyPlayer(player, "不可以接该任务！\n");
            return;
        }
        player.getQuestMap().get(QuestState.CAN_BUT_NOT).remove(questId);
        Quest quest = new Quest(questId);
        QuestType questType = QuestType.values()[quest.getQuestById().getType() - 1];
        player.getOnDoingQuest().computeIfAbsent(questType, key -> new ConcurrentHashMap<>());
        player.getOnDoingQuest().get(questType).put(questId, quest);
        playerData.updatePlayer(player);
        notifyScene.notifyPlayer(player, "成功接受该任务\n");
    }

    /**
     * 放弃任务
     */
    public void giveUpQuest(Player player, Integer questId) {
        QuestType questType = QuestType.values()[QuestResource.getQuestById(questId).getType() - 1];
        Quest quest = player.getOnDoingQuest().get(questType).get(questId);
        if (quest == null) {
            notifyScene.notifyPlayer(player, "您没有进行该任务\n");
            return;
        }
        if (quest.getQuestById().getTimes() == QuestTimes.ACHIEVEMENT.getType()) {
            notifyScene.notifyPlayer(player, "该类任务不可放弃\n");
            return;
        }
        player.getOnDoingQuest().get(questType).remove(questId);
        player.getQuestMap().get(QuestState.CAN_BUT_NOT).add(questId);
        playerData.updatePlayer(player);
        notifyScene.notifyPlayer(player, "已放弃该任务\n");
    }

    /**
     * 提交任务
     */
    public void commitQuest(Player player, int questId) {
        if (!player.getQuestMap().get(QuestState.COMPLETE).contains(questId)) {
            notifyScene.notifyPlayer(player, "您不能提交该任务\n");
            return;
        }
        QuestExcel questExcel = QuestResource.getQuestById(questId);
        Item item = new Item(questExcel.getItem());
        item.setNum(1);
        if (!itemService.getItem(item, player)) {
            long playerId = player.getTPlayer().getRoleId();
            Email email = emailService.createEmail(playerId, playerId, "任务奖励");
            email.getGifts().add(item);
            emailService.toReceiver(playerId, email);
        }
        playerService.getMoney(player, questExcel.getMoney());
        addSubmit(player, questId);
        playerData.updatePlayer(player);
        notifyScene.notifyPlayer(player, MessageFormat.format("获得奖励:{0},金币:{1}\n",
                item.getItemById().getName(), questExcel.getMoney()));

    }

    /**
     * 检查任务进度
     */
    public void checkQuestNoId(Player player, QuestType questType, int param) {
        player.getOnDoingQuest().computeIfAbsent(questType, key -> new ConcurrentHashMap<>());
        Iterator<Map.Entry<Integer, Quest>> iterator = player.getOnDoingQuest().get(questType).entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Quest> entry = iterator.next();
            Quest quest = entry.getValue();
            int progress = quest.getProgress().get(0);
            quest.getProgress().set(0, progress + param);
            checkIfCanComplete(player, quest, iterator);
        }
        playerData.updatePlayer(player);
    }

    /**
     * 检查任务进度（有id的任务）
     */
    public void checkQuestWithId(Player player, QuestType questType, int id, int num) {
        player.getOnDoingQuest().computeIfAbsent(questType, key -> new ConcurrentHashMap<>());
        Iterator<Map.Entry<Integer, Quest>> iterator = player.getOnDoingQuest().get(questType).entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Quest> entry = iterator.next();
            Quest quest = entry.getValue();
            IntStream.range(0, quest.getCfgList().size()).forEach((index) -> {
                if (quest.getCfgList().get(index).getId() == id) {
                    int nowProgress = quest.getProgress().get(index);
                    quest.getProgress().set(index, nowProgress + num);
                }
            });
            checkIfCanComplete(player, quest, iterator);
        }
        playerData.updatePlayer(player);
    }

    /**
     * 等级是否可接受别的任务
     */
    public void checkCanAcceptByLevel(Player player) {
        QuestResource.getQuestExcelMap().forEach((questId, questExcel) -> {
            if (questExcel.getLevel() <= player.getTPlayer().getLevel()) {
                if (questExcel.getPreQuest() == null || !checkIfHasComplete(player, questExcel.getPreQuest())) {
                    return;
                }
                player.getQuestMap().get(QuestState.CAN_BUT_NOT).add(questId);
            }
        });
    }

    /**
     * 是否可接受后置任务
     */
    public void checkCanAcceptByQuest(Player player, int questId) {
        QuestExcel questExcel = QuestResource.getQuestById(questId);
        if (player.getTPlayer().getLevel() >= questExcel.getLevel()) {
            player.getQuestMap().get(QuestState.CAN_BUT_NOT).add(questId);
        }
    }

    /**
     * 是否已经完成
     */
    public boolean checkIfHasComplete(Player player, int questId) {
        player.getQuestMap().computeIfAbsent(QuestState.COMPLETE, key -> new CopyOnWriteArrayList<>());
        player.getQuestMap().computeIfAbsent(QuestState.SUBMIT, key -> new CopyOnWriteArrayList<>());
        for (Integer key : player.getQuestMap().get(QuestState.SUBMIT)) {
            if (key == questId) {
                return true;
            }
        }
        for (Integer key : player.getQuestMap().get(QuestState.COMPLETE)) {
            if (key == questId) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查任务是否完成
     */
    public void checkIfCanComplete(Player player, Quest quest, Iterator<?> iterator) {
        for (int index = 0; index < quest.getCfgList().size(); index++) {
            if (quest.getProgress().get(index) < quest.getCfgList().get(index).getValue()) {
                return;
            }
            putMakeQuestComplete(player, quest, iterator);
            Integer posQuest = quest.getQuestById().getPosQuest();
            if (posQuest != null) {
                checkCanAcceptByQuest(player, posQuest);
            }
        }
        notifyScene.notifyPlayer(player, MessageFormat.format("完成任务{0}\n", quest.getQuestById().getName()));
    }


    /**
     * 把任务转移到已完成未提交的列表里
     */
    public void putMakeQuestComplete(Player player, Quest quest, Iterator<?> iterator) {
        iterator.remove();
        player.getQuestMap().computeIfAbsent(QuestState.COMPLETE, key -> new CopyOnWriteArrayList<>());
        player.getQuestMap().get(QuestState.COMPLETE).add(quest.getId());
    }

    /**
     * 任务转移至提交
     */
    public void addSubmit(Player player, Integer questId) {
        player.getQuestMap().computeIfAbsent(QuestState.SUBMIT, key -> new CopyOnWriteArrayList<>());
        player.getQuestMap().get(QuestState.COMPLETE).remove(questId);
        player.getQuestMap().get(QuestState.SUBMIT).add(questId);
    }
}

