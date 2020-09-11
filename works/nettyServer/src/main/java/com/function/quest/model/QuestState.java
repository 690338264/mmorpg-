package com.function.quest.model;

/**
 * @author Catherine
 * @create 2020-09-11 10:50
 */
public enum QuestState {
    //已提交
    SUBMIT(1),
    //完成
    COMPLETE(2),
    //正在进行
    ONGOING(3),
    //可以接受但是没接
    CAN_BUT_NOT(4),
    //未达到接受条件
    CANNOT(5),
    ;
    int type;

    QuestState(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
