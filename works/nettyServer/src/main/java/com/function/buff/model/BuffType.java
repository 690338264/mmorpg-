package com.function.buff.model;

/**
 * @author Catherine
 * @create 2020-09-15 10:06
 */
public enum BuffType {
    //对敌人
    TO_ENEMY(1),
    //对自己人
    FRIEND(2),
    ;
    int type;

    BuffType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
