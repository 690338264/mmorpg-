package com.function.monster.model;

import lombok.Data;

@Data
public class Monster {
    private Integer id;

    private Integer selfHp;

    private Integer sceneId;

    private Integer status;

    public MonsterExcel getMonsterExcel() {
        return MonsterResource.getMonById(id);
    }

}
