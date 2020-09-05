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

    public TPlayerInfo(long playerId, String name, int occupation) {
        this.playerId = playerId;
        this.name = name;
        this.occupation = occupation;
    }

    public long getPlayerId() {
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

    public int getSectPosition() {
        return sectPosition;
    }

    public void setSectPosition(int sectPosition) {
        this.sectPosition = sectPosition;
    }

    @Override
    public String toString() {
        return "TPlayerInfo{" +
                "playerId=" + playerId +
                ", name='" + name + '\'' +
                ", occupation=" + occupation +
                ", sectPosition=" + sectPosition +
                '}';
    }
}
