package com.function.scene.model;

/**
 * @author Catherine
 * @create 2020-08-27 23:40
 */
public enum SceneType {
    //公共地图
    PUBLIC(1),
    //个人地图
    PRIVATE(2),
    //小队地图
    TEAM(3),
    ;
    int type;

    SceneType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
