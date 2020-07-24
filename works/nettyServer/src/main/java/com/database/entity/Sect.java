package com.database.entity;

import java.io.Serializable;

public class Sect implements Serializable {
    private Integer id;

    private String name;

    private Integer memnum;

    private Integer level;

    private Long fame;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getMemnum() {
        return memnum;
    }

    public void setMemnum(Integer memnum) {
        this.memnum = memnum;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Long getFame() {
        return fame;
    }

    public void setFame(Long fame) {
        this.fame = fame;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", memnum=").append(memnum);
        sb.append(", level=").append(level);
        sb.append(", fame=").append(fame);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}