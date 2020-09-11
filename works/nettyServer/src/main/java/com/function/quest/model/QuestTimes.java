package com.function.quest.model;

/**
 * @author Catherine
 * @create 2020-09-11 10:37
 */
public enum QuestTimes {
    //
    ACHIEVEMENT(1),
    MAINLINE(2),
    SIDELINE(3),
    ;
    int type;

    QuestTimes(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
