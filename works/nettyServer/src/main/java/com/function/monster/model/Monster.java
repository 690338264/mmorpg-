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
    private Integer id;


    /**
     * 怪物场景自己的id
     */
    private Integer sceneId;
    /**
     * 仇恨列表
     */
    private Map<Long, Integer> hurtList = new ConcurrentHashMap<>();

    @Override
    public int getOriHp() {
        return getMonsterExcel().getHp();
    }

    @Override
    public String getName() {
        return getMonsterExcel().getName();
    }

    public MonsterExcel getMonsterExcel() {
        return MonsterResource.getMonById(id);
    }

}
