package com.function.monster.model;

import com.function.monster.excel.MonsterExcel;
import com.function.monster.excel.MonsterResource;
import lombok.Data;

/**
 * @author Catherine
 */
@Data
public class Monster {
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

    public MonsterExcel getMonsterExcel() {
        return MonsterResource.getMonById(id);
    }

}
