package com.function.email.model;

/**
 * @author Catherine
 * @create 2020-09-02 17:32
 */
public enum EmailState {
    //
    UNREAD(1, "[未读]"),
    READ(2, "[已读]"),
    ;
    Integer type;
    String out;

    EmailState(Integer type, String out) {
        this.type = type;
        this.out = out;
    }

    public Integer getType() {
        return type;
    }

    public String getOut() {
        return out;
    }
}
