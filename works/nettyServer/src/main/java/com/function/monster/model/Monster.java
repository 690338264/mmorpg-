package com.function.monster.model;

import com.function.monster.excel.MonsterExcel;
import com.function.monster.excel.MonsterResource;
import com.function.scene.model.SceneObject;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Catherine
 */
@Data
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

}
