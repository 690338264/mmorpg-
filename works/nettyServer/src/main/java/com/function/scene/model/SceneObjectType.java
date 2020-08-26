package com.function.scene.model;

/**
 * @author Catherine
 * @create 2020-08-26 15:52
 */
public enum SceneObjectType {

    //NPC
    NPC(1),

    //怪物
    MONSTER(2),

    //玩家
    PLAYER(3),
    ;
    Integer type;

    SceneObjectType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

}
