package com.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Catherine
 * @create 2020-09-05 21:25
 */
@Entity
@Table(name = "playerInfo")
public class TPlayerInfo {
    @Column(name = "userId")
    private long userId;
    @Id
    @Column(name = "playerId")
    private long playerId;

    @Column(name = "name")
    private String name;

    @Column(name = "occupation")
    private int occupation;

    @Column(name = "sectPosition")
    private Integer sectPosition;

    public TPlayerInfo() {
    }

    public TPlayerInfo(long userId, long playerId, String name, int occupation) {
        this.userId = userId;
        this.playerId = playerId;
        this.name = name;
        this.occupation = occupation;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOccupation() {
        return occupation;
    }

    public void setOccupation(int occupation) {
        this.occupation = occupation;
    }

    public Integer getSectPosition() {
        return sectPosition;
    }

    public void setSectPosition(Integer sectPosition) {
        this.sectPosition = sectPosition;
    }

    @Override
    public String toString() {
        return "TPlayerInfo{" +
                "userId=" + userId +
                ", playerId=" + playerId +
                ", name='" + name + '\'' +
                ", occupation=" + occupation +
                ", sectPosition=" + sectPosition +
                '}';
    }
}
