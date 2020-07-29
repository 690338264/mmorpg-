package com.function.monster.model;

import com.function.skill.model.Skill;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class MonsterExcel {

    private int id;

    private String name;

    private int hp;

    private int mp;

    private int aggr;

    private String skill;

    private int reviveTime;

    private int drop;

    private Map<Integer, Skill> monsterSkill = new HashMap<Integer, Skill>();
}
