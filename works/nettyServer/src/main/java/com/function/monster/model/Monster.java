package com.function.monster.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.function.monster.excel.MonsterExcel;
import com.function.monster.excel.MonsterResource;
import com.function.player.model.Player;
import com.function.scene.model.SceneObject;
import lombok.Data;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Catherine
 */
@Data
@JsonIgnoreProperties(value = {"timer"})
public class Monster extends SceneObject {
    /**
     * 怪物类型id
     */
    private Integer id;

    ReentrantLock lock = new ReentrantLock();

    /**
     * 怪物场景自己的id
     */
    private Integer sceneId;
    /**
     * 当前仇恨目标
     */
    private Player target;

    @Override
    public int getOriHp() {
        return getMonsterExcel().getHp();
    }

    public MonsterExcel getMonsterExcel() {
        return MonsterResource.getMonById(id);
    }

}
