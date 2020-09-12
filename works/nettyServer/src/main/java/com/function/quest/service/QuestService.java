package com.function.quest.service;

import com.function.player.model.Player;
import com.function.quest.model.Quest;
import com.function.quest.model.QuestState;
import com.function.quest.model.QuestType;
import com.function.scene.service.NotifyScene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

/**
 * @author Catherine
 * @create 2020-09-12 15:05
 */
@Component
public class QuestService {
    @Autowired
    private NotifyScene notifyScene;

    public void showQuest(Player player) {
        player.getQuestMap().get(QuestState.ONGOING).forEach((questType, quests) -> {
            quests.forEach((questId, quest) -> {
                notifyScene.notifyPlayer(player, MessageFormat.format("{0}\n{1}\n",
                        quest.getQuestById().getName(), quest.getQuestById().getText()));
            });
        });
    }

    public void checkQuestNoId(Player player, QuestType questType, int param) {
        player.getQuestMap().computeIfAbsent(QuestState.ONGOING, key -> new ConcurrentHashMap<>());
        Map<QuestType, Map<Integer, Quest>> questMap = player.getQuestMap().get(QuestState.ONGOING);
        questMap.computeIfAbsent(questType, key -> new ConcurrentHashMap<>());
        questMap.get(questType).forEach((questId, quest) -> {
            int progress = quest.getProgress().get(0);
            quest.getProgress().set(0, progress + param);
            checkIfComplete(player, quest, questType);
        });
//        player.getTodoQuest().computeIfAbsent(questType, key -> new ConcurrentHashMap<>());
//        player.getTodoQuest().get(questType).forEach((questId, quest) -> {
//            int progress = quest.getProgress().get(0);
//            quest.getProgress().set(0, progress + param);
//        });
    }

    public void checkQuestWithId(Player player, QuestType questType, int id, int num) {
        player.getQuestMap().computeIfAbsent(QuestState.ONGOING, key -> new ConcurrentHashMap<>());
        Map<QuestType, Map<Integer, Quest>> questMap = player.getQuestMap().get(QuestState.ONGOING);
        questMap.computeIfAbsent(questType, key -> new ConcurrentHashMap<>());
        questMap.get(questType).forEach((questId, quest) -> {
            IntStream.range(0, quest.getCfgList().size()).forEach((index) -> {
                if (quest.getCfgList().get(index).getId() == id) {
                    int nowProgress = quest.getProgress().get(index);
                    quest.getProgress().set(index, nowProgress + num);
                }
            });
            checkIfComplete(player, quest, questType);
        });
//        player.getTodoQuest().computeIfAbsent(questType, key -> new ConcurrentHashMap<>());
//        player.getTodoQuest().get(questType).forEach((questId, quest) -> {
//            IntStream.range(0, quest.getCfgList().size()).forEach((index) -> {
//                if (quest.getCfgList().get(index).getId() == id) {
//                    int nowProgress = quest.getProgress().get(index);
//                    quest.getProgress().set(index, nowProgress + num);
//                }
//            });
//        });
    }

    /**
     * 检查任务是否完成
     */
    public void checkIfComplete(Player player, Quest quest, QuestType questType) {
        if (quest.getCfgList() == null) {
            int target = Integer.parseInt(quest.getQuestById().getTarget());
            if (quest.getProgress().get(0) >= target) {
                putMakeQuestComplete(player, quest, questType);
            }
        } else {
            for (int index = 0; index < quest.getCfgList().size(); index++) {
                if (quest.getProgress().get(index) < quest.getCfgList().get(index).getValue()) {
                    return;
                }
            }
            putMakeQuestComplete(player, quest, questType);
            notifyScene.notifyPlayer(player, MessageFormat.format("完成任务{0}\n", quest.getQuestById().getName()));
        }
    }

    /**
     * 把任务转移到已完成未提交的列表里
     */
    public void putMakeQuestComplete(Player player, Quest quest, QuestType questType) {
        player.getQuestMap().get(QuestState.ONGOING).get(questType).remove(quest.getId());
        player.getQuestMap().computeIfAbsent(QuestState.COMPLETE, key -> new ConcurrentHashMap<>());
        Map<QuestType, Map<Integer, Quest>> questMap = player.getQuestMap().get(QuestState.COMPLETE);
        questMap.computeIfAbsent(questType, key -> new ConcurrentHashMap<>());
        player.getQuestMap().get(QuestState.COMPLETE).get(questType).put(quest.getId(), quest);
    }

}

