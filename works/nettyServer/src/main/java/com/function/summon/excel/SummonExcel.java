package com.function.summon.excel;

import com.function.skill.model.Skill;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Catherine
 * @create 2020-09-23 11:43
 */
public class SummonExcel {
    private int id;

    private String name;

    private int atk;

    private int def;

    private int speed;

    private String skill;

    private Map<Integer, Skill> SummonSkill = new HashMap<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public int getDef() {
        return def;
    }

    public void setDef(int def) {
        this.def = def;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public Map<Integer, Skill> getSummonSkill() {
        return SummonSkill;
    }

    public void setSummonSkill(Map<Integer, Skill> summonSkill) {
        SummonSkill = summonSkill;
    }
}
