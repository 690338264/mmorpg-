package com.function.quest.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.function.quest.excel.QuestExcel;
import com.function.quest.excel.QuestResource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author Catherine
 * @create 2020-09-11 15:25
 */
public class Quest {
    public Quest(int id) {
        this.id = id;
        if (getQuestById().getType() == QuestType.ITEM_GET.type ||
                getQuestById().getType() == QuestType.MONSTER_KILL.type ||
                getQuestById().getType() == QuestType.NPC_TALK.type) {
            cfgList = JSON.parseObject(getQuestById().getTarget(), new TypeReference<List<QuestCfg>>() {
            });
            IntStream.range(0, cfgList.size()).forEach((index) ->
                    progress.set(index, 0));
        } else {
            progress.add(0, 0);
        }
    }

    private int id;

    private List<Integer> progress = new ArrayList<>();

    private List<QuestCfg> cfgList = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<QuestCfg> getCfgList() {
        return cfgList;
    }

    public void setCfgList(List<QuestCfg> cfgList) {
        this.cfgList = cfgList;
    }

    public List<Integer> getProgress() {
        return progress;
    }

    public void setProgress(List<Integer> progress) {
        this.progress = progress;
    }

    public QuestExcel getQuestById() {
        return QuestResource.getQuestById(id);
    }
}
