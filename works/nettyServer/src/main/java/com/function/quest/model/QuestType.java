package com.function.quest.model;

/**
 * @author Catherine
 * @create 2020-09-10 20:30
 */
public enum QuestType {
    //
    LEVEL(1),
    MONEY(2),
    NPC(3),
    ITEM(4),
    MONSTER(5),
    ;
    Integer type;

    QuestType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }
}
