package com.function.quest.model;

/**
 * @author Catherine
 * @create 2020-09-10 20:30
 */
public enum QuestType {
    //
    LEVEL_UP(1),
    MONEY_GET(2),
    NPC_TALK(3),
    ITEM_GET(4),
    MONSTER_KILL(5),
    ;
    int type;

    QuestType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}