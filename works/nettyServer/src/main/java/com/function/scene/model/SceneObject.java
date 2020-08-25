package com.function.scene.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * @author Catherine
 * @create 2020-08-13 16:07
 */
public class SceneObject {
    /**
     * 当前hp
     */
    private int hp;
    private int atk;
    private Map<String, ScheduledFuture> taskMap = new ConcurrentHashMap<>();

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

    public Map<String, ScheduledFuture> getTaskMap() {
        return taskMap;
    }

    public void setTaskMap(Map<String, ScheduledFuture> taskMap) {
        this.taskMap = taskMap;
    }
}
