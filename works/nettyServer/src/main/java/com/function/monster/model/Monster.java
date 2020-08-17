package com.function.monster.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.function.monster.excel.MonsterExcel;
import com.function.monster.excel.MonsterResource;
import com.function.player.model.Player;
import com.function.scene.model.SceneObject;
import com.function.skill.model.Skill;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

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
    /**
     * 怪物血量
     */
    private Integer selfHp;
    /**
     * 怪物场景自己的id
     */
    private Integer sceneId;
    /**
     * 死亡复活计时器
     */
    private Timer timer;
    /**
     * 当前仇恨目标
     */
    private Player target;

    private Map<Integer, Skill> canUseSkill = new HashMap<>();

    public MonsterExcel getMonsterExcel() {
        return MonsterResource.getMonById(id);
    }

}
