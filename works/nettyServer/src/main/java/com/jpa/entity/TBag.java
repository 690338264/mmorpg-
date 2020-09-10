package com.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Catherine
 * @create 2020-08-11 21:24
 */
@Entity
@Table(name = "bag")
public class TBag {
    @Column(name = "volume")
    private int volume;

    @Column(name = "item", columnDefinition = "TEXT")
    private String item;
    @Id
    @Column(name = "playerId")
    private long playerId;

    @Column(name = "maxId")
    private long maxId;

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public Long getMaxId() {
        return maxId;
    }

    public void setMaxId(Long maxId) {
        this.maxId = maxId;
    }

    @Override
    public String toString() {
        return "TBag{" +
                "volume=" + volume +
                ", item='" + item + '\'' +
                ", playerId=" + playerId +
                ", maxId=" + maxId +
                '}';
    }
}
