package com.jpa.entity;

import javax.persistence.*;

/**
 * @author Catherine
 * @create 2020-09-08 11:17
 */
@Entity
@Table(name = "auction")
public class TAuction {
    @Id
    @GeneratedValue
    @Column(name = "auctionId")
    private Long auctionId;

    @Column(name = "type")
    private int type;

    @Column(name = "item", columnDefinition = "TEXT")
    private String item;

    @Column(name = "auctioneer")
    private long auctioneer;

    @Column(name = "bidder")
    private Long bidder;

    @Column(name = "highestMoney")
    private int highestMoney;

    @Column(name = "finishTime")
    private long finishTime;

    public Long getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(Long auctionId) {
        this.auctionId = auctionId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public long getAuctioneer() {
        return auctioneer;
    }

    public void setAuctioneer(long auctioneer) {
        this.auctioneer = auctioneer;
    }

    public Long getBidder() {
        return bidder;
    }

    public void setBidder(Long bidder) {
        this.bidder = bidder;
    }

    public int getHighestMoney() {
        return highestMoney;
    }

    public void setHighestMoney(int highestMoney) {
        this.highestMoney = highestMoney;
    }

    public long getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(long finishTime) {
        this.finishTime = finishTime;
    }

    @Override
    public String toString() {
        return "TAuction{" +
                "auctionId=" + auctionId +
                ", type=" + type +
                ", item='" + item + '\'' +
                ", auctioneer='" + auctioneer + '\'' +
                ", bidder='" + bidder + '\'' +
                ", highestMoney=" + highestMoney +
                ", finishTime=" + finishTime +
                '}';
    }
}
