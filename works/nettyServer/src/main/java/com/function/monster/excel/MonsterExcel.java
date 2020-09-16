package com.function.monster.excel;


import com.function.item.excel.ItemExcel;
import com.function.skill.model.Skill;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    private int speed;

    private int def;
    /**
     * 怪物技能
     */
    private String skill;
    /**
     * 复活时间
     */
    private long reviveTime;
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
    /**
     * 掉落列表
     */
    private List<ItemExcel> itemList = new ArrayList<>();
}
