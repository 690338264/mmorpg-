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

    SUMMON(4),
    ;
    int type;

    SceneObjectType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

}
