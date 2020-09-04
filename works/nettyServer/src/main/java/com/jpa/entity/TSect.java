package com.jpa.entity;

import javax.persistence.*;

/**
 * @author Catherine
 * @create 2020-09-05 00:48
 */
@Entity
@Table(name = "sect")
public class TSect {
    @Id
    @GeneratedValue
    @Column(name = "sectId")
    private int sectId;

    @Column(name = "name")
    private String name;

    @Column(name = "member")
    private String member;

    @Column(name = "level")
    private int level;

    @Column(name = "warehouse")
    private String warehouse;

    @Column(name = "join")
    private String join;

    public TSect(String name) {
        this.level = 1;
        this.name = name;
    }

    public int getSectId() {
        return sectId;
    }

    public void setSectId(int sectId) {
        this.sectId = sectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getJoin() {
        return join;
    }

    public void setJoin(String join) {
        this.join = join;
    }

    @Override
    public String toString() {
        return "TSect{" +
                "sectId=" + sectId +
                ", name='" + name + '\'' +
                ", member='" + member + '\'' +
                ", level=" + level +
                ", warehouse='" + warehouse + '\'' +
                ", join='" + join + '\'' +
                '}';
    }
}
