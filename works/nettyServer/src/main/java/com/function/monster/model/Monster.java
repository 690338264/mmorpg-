package com.function.monster.model;

import com.function.monster.excel.MonsterExcel;
import com.function.monster.excel.MonsterResource;
import com.function.scene.model.SceneObject;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

/**
 * @author Catherine
 */
@Data
public class Monster extends SceneObject {
    private Integer id;
    /**
     * 怪物血量
     */
    private Integer selfHp;
    /**
     * 怪物所在场景
     */
    private Integer sceneId;

    private Integer status;

    private Map<String, Timer> timerMap = new HashMap<>();

    public MonsterExcel getMonsterExcel() {
        return MonsterResource.getMonById(id);
    }

}
