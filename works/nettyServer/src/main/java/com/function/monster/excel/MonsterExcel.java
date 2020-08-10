package com.function.monster.excel;


import com.function.skill.model.Skill;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Catherine
 */
@Data
public class MonsterExcel {

    private int id;

    private String name;

    private int hp;

    private int mp;
    /**
     * 怪物攻击力
     */
    private int aggr;
    /**
     * 怪物技能
     */
    private String skill;
    /**
     * 复活时间
     */
    private int reviveTime;
    /**
     * 掉落
     */
    private String drop;
    /**
     * 掉落金币
     */
    private int money;
    /**
     * 经验值
     */
    private int exc;

    /**
     * 技能列表
     */
    private Map<Integer, Skill> monsterSkill = new HashMap<>();
}
