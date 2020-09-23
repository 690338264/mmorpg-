package com.function.monster.model;

import com.function.monster.excel.MonsterExcel;
import com.function.monster.excel.MonsterResource;
import com.function.player.model.SceneObjectTask;
import com.function.scene.model.Scene;
import com.function.scene.model.SceneObject;
import com.function.scene.model.SceneObjectState;
import com.function.scene.model.SceneObjectType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Catherine
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Monster extends SceneObject {
    /**
     * 怪物类型id
     */
    private int excelId;
    /**
     * 怪物场景自己的id
     */
    private Long id;
    /**
     * 仇恨列表
     */
    private Map<Long, Integer> hurtList = new ConcurrentHashMap<>();
    /**
     * 阵亡时间
     */
    private Long deathTime;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public Map<String, Object> getAttributeMap() {
        Map<String, Object> attributeMap = new HashMap<>();
        attributeMap.put("atk", getMonsterExcel().getAggr());
        attributeMap.put("speed", getMonsterExcel().getSpeed());
        attributeMap.put("def", getMonsterExcel().getDef());
        return attributeMap;
    }

    @Override
    public int getOriHp() {
        return getMonsterExcel().getHp();
    }

    @Override
    public String getName() {
        return getMonsterExcel().getName();
    }

    public MonsterExcel getMonsterExcel() {
        return MonsterResource.getMonById(excelId);
    }

    @Override
    public void onDie() {
        setState(SceneObjectState.DEATH);
        Scene scene = getNowScene();
        setDeathTime(System.currentTimeMillis());

        getBuffs().forEach((k, v) -> {
            v.cancel(true);
            getBuffs().remove(k);
        });

        scene.getSceneObjectMap().get(SceneObjectType.MONSTER).remove(id);
        scene.getWaitForRevive().put(id, this);

        Map<Long, SceneObject> summonMap = scene.getSceneObjectMap().get(SceneObjectType.SUMMON);
        getHurtList().forEach((attackerId, hurt) -> {
            if (summonMap.containsKey(attackerId)) {
                summonMap.get(attackerId).getTaskMap().get(SceneObjectTask.ATTACK).cancel(true);
                summonMap.get(attackerId).getTaskMap().remove(SceneObjectTask.ATTACK);
            }
        });
        getHurtList().clear();
    }
}
