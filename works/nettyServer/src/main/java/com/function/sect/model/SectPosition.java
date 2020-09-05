package com.function.sect.model;

/**
 * @author Catherine
 * @create 2020-09-05 14:29
 */
public enum SectPosition {
    //会长
    PRESIDENT(1, "会长"),
    //副会长
    VICE_PRESIDENT(2, "副会长"),
    //精英
    ELITE(3, "精英"),
    //普通会员
    NORMAL_MEMBER(4, "普通会员"),
    ;
    String role;
    Integer type;

    SectPosition(Integer type, String role) {
        this.type = type;
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public Integer getType() {
        return type;
    }
}
