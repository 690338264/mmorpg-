package com.function.scene.model;

/**
 * @author Catherine
 * @create 2020-09-16 11:27
 */
public enum SceneObjectState {
    //正常状态
    NORMAL(1),
    //死亡
    DEATH(2),
    //晕
    DIZZY(3),
    ;
    int type;

    SceneObjectState(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

}
