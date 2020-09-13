package com.function.quest.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.annotation.JSONField;
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
    public Quest() {
    }

    public Quest(int id) {
        this.id = id;
        cfgList = JSON.parseObject(getQuestById().getTarget(), new TypeReference<List<QuestCfg>>() {
        });
        IntStream.range(0, cfgList.size()).forEach((index) -> progress.add(0));
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

    @JSONField(serialize = false)
    public QuestExcel getQuestById() {
        return QuestResource.getQuestById(id);
    }
}
