package com.function.scene.model;

import com.function.player.model.SceneObjectTask;
import com.function.skill.model.Skill;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Catherine
 * @create 2020-08-13 16:07
 */
@SuppressWarnings("rawtypes")
public abstract class SceneObject {

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
    private SceneObjectType type;
    /**
     * 原始Hp
     */
    private int oriHp;

    private int mp;
    /**
     * 所在地
     */
    private Scene nowScene;

    private SceneObjectState state = SceneObjectState.NORMAL;

    private ReentrantLock lock = new ReentrantLock(true);
    /**
     * 线程任务列表
     */
    private final Map<SceneObjectTask, ScheduledFuture> taskMap = new ConcurrentHashMap<>();
    /**
     * buff
     */
    private final Map<Integer, ScheduledFuture> buffs = new ConcurrentHashMap<>();
    /**
     * 可用技能
     */
    private final Map<Integer, Skill> canUseSkill = new HashMap<>();

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

    public SceneObjectType getType() {
        return type;
    }

    public void setType(SceneObjectType type) {
        this.type = type;
    }

    public int getOriHp() {
        return oriHp;
    }

    public void setOriHp(int oriHp) {
        this.oriHp = oriHp;
    }

    public int getMp() {
        return mp;
    }

    public void setMp(int mp) {
        this.mp = mp;
    }

    public Scene getNowScene() {
        return nowScene;
    }

    public void setNowScene(Scene nowScene) {
        this.nowScene = nowScene;
    }

    public ReentrantLock getLock() {
        return lock;
    }

    public void setLock(ReentrantLock lock) {
        this.lock = lock;
    }

    public Map<SceneObjectTask, ScheduledFuture> getTaskMap() {
        return taskMap;
    }

    public Map<Integer, ScheduledFuture> getBuffs() {
        return buffs;
    }

    public Map<Integer, Skill> getCanUseSkill() {
        return canUseSkill;
    }

    public SceneObjectState getState() {
        return state;
    }

    public void setState(SceneObjectState state) {
        this.state = state;
    }

    /**
     * 场景物体的id
     *
     * @return player/monster的id
     */
    public abstract Long getId();

    /**
     * 属性列表
     */
    public abstract Map<String, Object> getAttributeMap();

    public void onDie() {
    }

    public void onBeAttack() {
    }

}
