package com.function.monster.model;

import com.function.monster.excel.MonsterExcel;
import com.function.monster.excel.MonsterResource;
import lombok.Data;

@Data
public class Monster {
    private Integer id;

    private Integer selfHp;

    private Integer sceneId;

    private Integer status;

    private Long dieTime;

    public MonsterExcel getMonsterExcel() {
        return MonsterResource.getMonById(id);
    }

}
