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
    Integer type;

    ItemType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }
}
