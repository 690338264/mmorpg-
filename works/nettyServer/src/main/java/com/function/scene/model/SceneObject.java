package com.function.scene.model;

import com.function.skill.model.Skill;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * @author Catherine
 * @create 2020-08-13 16:07
 */
public class SceneObject {

    private String name;
    /**
     * 当前hp
     */
    private int hp;
    /**
     * 当前攻击力
     */
    private int atk;
    /**
     * 种类
     */
    private int type;
    /**
     * 原始Hp
     */
    private int oriHp;
    /**
     * 线程任务列表
     */
    private Map<String, ScheduledFuture> taskMap = new ConcurrentHashMap<>();
    /**
     * buff
     */
    private Map<Integer, ScheduledFuture> buffs = new ConcurrentHashMap<>();
    /**
     * 可用技能
     */
    private Map<Integer, Skill> canUseSkill = new HashMap<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getOriHp() {
        return oriHp;
    }

    public void setOriHp(int oriHp) {
        this.oriHp = oriHp;
    }

    public Map<String, ScheduledFuture> getTaskMap() {
        return taskMap;
    }


    public void setTaskMap(Map<String, ScheduledFuture> taskMap) {
        this.taskMap = taskMap;
    }

    public Map<Integer, ScheduledFuture> getBuffs() {
        return buffs;
    }

    public void setBuffs(Map<Integer, ScheduledFuture> buffs) {
        this.buffs = buffs;
    }

    public Map<Integer, Skill> getCanUseSkill() {
        return canUseSkill;
    }

    public void setCanUseSkill(Map<Integer, Skill> canUseSkill) {
        this.canUseSkill = canUseSkill;
    }


}
