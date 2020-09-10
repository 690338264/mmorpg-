package com.jpa.entity;

import javax.persistence.*;

/**
 * @author Catherine
 * @create 2020-09-05 00:48
 */
@Entity
@Table(name = "sect", uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
public class TSect {
    @Id
    @GeneratedValue
    @Column(name = "sectId")
    private Long sectId;
    @Column(name = "name")
    private String name;

    @Column(name = "member", columnDefinition = "TEXT")
    private String member;

    @Column(name = "level")
    private int level;

    @Column(name = "warehouse", columnDefinition = "TEXT")
    private String warehouse;

    @Column(name = "joinRequest", columnDefinition = "TEXT")
    private String joinRequest;

    public TSect() {
    }

    public TSect(String name) {
        this.level = 1;
        this.name = name;
    }

    public Long getSectId() {
        return sectId;
    }

    public void setSectId(Long sectId) {
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

    public String getJoinRequest() {
        return joinRequest;
    }

    public void setJoinRequest(String joinRequest) {
        this.joinRequest = joinRequest;
    }

    @Override
    public String toString() {
        return "TSect{" +
                "sectId=" + sectId +
                ", name='" + name + '\'' +
                ", member='" + member + '\'' +
                ", level=" + level +
                ", warehouse='" + warehouse + '\'' +
                ", join='" + joinRequest + '\'' +
                '}';
    }
}
