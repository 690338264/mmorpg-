package com.function.item.model;

/**
 * @author Catherine
 * @create 2020-09-01 15:08
 */
public enum ItemType {
    //药品
    MEDICINAL(1),
    //装备
    EQUIPMENT(2),
    ;
    int type;

    ItemType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
