package com.function.quest.service;

import com.function.player.model.Player;
import com.function.player.service.PlayerData;
import com.function.quest.model.Quest;
import com.function.quest.model.QuestState;
import com.function.quest.model.QuestType;
import com.function.scene.service.NotifyScene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
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

    public void showQuest(Player player) {

    }

    /**
     * 检查任务进度
     */
    public void checkQuestNoId(Player player, QuestType questType, int param) {
        player.getOnDoingQuest().computeIfAbsent(questType, key -> new ConcurrentHashMap<>());
        player.getOnDoingQuest().get(questType).forEach((questId, quest) -> {
            int progress = quest.getProgress().get(0);
            quest.getProgress().set(0, progress + param);
            checkIfComplete(player, quest, questType);
        });
        playerData.updatePlayer(player);
    }

    /**
     * 检查任务进度（有id的任务）
     */
    public void checkQuestWithId(Player player, QuestType questType, int id, int num) {
        player.getOnDoingQuest().computeIfAbsent(questType, key -> new ConcurrentHashMap<>());
        player.getOnDoingQuest().get(questType).forEach((questId, quest) -> {
            IntStream.range(0, quest.getCfgList().size()).forEach((index) -> {
                if (quest.getCfgList().get(index).getId() == id) {
                    int nowProgress = quest.getProgress().get(index);
                    quest.getProgress().set(index, nowProgress + num);
                }
            });
            checkIfComplete(player, quest, questType);
        });
        playerData.updatePlayer(player);
    }

    /**
     * 检查任务是否完成
     */
    public void checkIfComplete(Player player, Quest quest, QuestType questType) {
        for (int index = 0; index < quest.getCfgList().size(); index++) {
            if (quest.getProgress().get(index) < quest.getCfgList().get(index).getValue()) {
                return;
            }
            putMakeQuestComplete(player, quest, questType);
            notifyScene.notifyPlayer(player, MessageFormat.format("完成任务{0}\n", quest.getQuestById().getName()));
        }
    }

    /**
     * 把任务转移到已完成未提交的列表里
     */
    public void putMakeQuestComplete(Player player, Quest quest, QuestType questType) {
        player.getOnDoingQuest().get(questType).remove(quest.getId());
        player.getQuestMap().computeIfAbsent(QuestState.COMPLETE, key -> new CopyOnWriteArrayList<>());
        player.getQuestMap().get(QuestState.COMPLETE).add(quest.getId());
    }

}

