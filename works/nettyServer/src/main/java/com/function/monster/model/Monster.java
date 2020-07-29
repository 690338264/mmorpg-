package com.function.monster.model;

import com.function.skill.model.Skill;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Monster{
    private Integer id;

    private Integer selfHp;

    private Integer sceneId;

    //private MonsterExcel monsterExcel;

    private Integer status;

    public MonsterExcel getMonsterExcel(){
        return MonsterResource.getMonById(id);
    }

}
