package com.function.player.model;

/**
 * @author Catherine
 * @create 2020-09-01 16:47
 */
public enum SceneObjectTask {
    //
    MP_ADD(1),
    ATTACK(2),
    UPDATE_BAG(3),
    SKILL_CD(4),
    ;
    Integer key;

    SceneObjectTask(Integer key) {
        this.key = key;
    }

    public Integer getKey() {
        return key;
    }
}
