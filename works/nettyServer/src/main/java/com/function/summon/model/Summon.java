package com.function.summon.model;

import com.function.scene.model.SceneObject;
import com.function.scene.model.SceneObjectType;
import com.function.summon.excel.SummonExcel;
import com.function.summon.excel.SummonResource;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Catherine
 * @create 2020-09-23 12:42
 */
public class Summon extends SceneObject {
    private final int excelId;

    private long id;

    public Summon(int excelId) {
        this.excelId = excelId;
        setType(SceneObjectType.SUMMON);
        getCanUseSkill().putAll(getById().getSummonSkill());
        setName(getById().getName());
    }

    public void setId(long id) {
        this.id = id;
    }

    public SummonExcel getById() {
        return SummonResource.getMonById(excelId);
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public Map<String, Object> getAttributeMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("atk", getById().getAtk());
        map.put("def", getById().getDef());
        map.put("speed", getById().getSpeed());
        return map;
    }

    @Override
    public void onDie() {
        getTaskMap().forEach((key, task) -> task.cancel(true));
        getNowScene().getSceneObjectMap().get(SceneObjectType.SUMMON).remove(id);
    }
}
