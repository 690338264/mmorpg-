package com.function.quest.model;

/**
 * @author Catherine
 * @create 2020-09-10 20:30
 */
public enum QuestType {
    //
    LEVEL_UP(1),
    FRIEND_ADD(2),
    MONEY_GET(3),
    NPC_TALK(4),
    ITEM_GET(5),
    MONSTER_KILL(6),
    DUNGEON_PASS(7),
    TEAM_MAKE(8),
    SECT_ADD(9),
    PK_WIN(10),
    TRADE(11),
    ;
    int type;

    QuestType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
