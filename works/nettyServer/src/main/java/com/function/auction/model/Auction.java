package com.function.auction.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.function.item.model.Item;
import com.function.player.model.Player;
import com.jpa.entity.TAuction;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Catherine
 * @create 2020-09-08 11:36
 */
public class Auction {

    private TAuction tAuction;
    /**
     * 拍卖物
     */
    private Item item;
    /**
     * 拍卖人
     */
    private Player auctioneer;
    /**
     * 出价人
     */
    private Player bidder;

    private ScheduledFuture update;

    private AtomicBoolean isSelling = new AtomicBoolean();

    public Auction(TAuction tAuction) {
        this.tAuction = tAuction;
        isSelling.set(false);
    }

    public void fromJson() {
        item = JSON.parseObject(tAuction.getItem(), new TypeReference<Item>() {
        });
        auctioneer = JSON.parseObject(tAuction.getAuctioneer(), new TypeReference<Player>() {
        });
        bidder = JSON.parseObject(tAuction.getBidder(), new TypeReference<Player>() {
        });
    }

    public void toJson() {
        tAuction.setItem(JSON.toJSONString(item));
        tAuction.setAuctioneer(JSON.toJSONString(auctioneer));
        tAuction.setBidder(JSON.toJSONString(bidder));
    }

    public TAuction gettAuction() {
        return tAuction;
    }

    public void settAuction(TAuction tAuction) {
        this.tAuction = tAuction;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Player getAuctioneer() {
        return auctioneer;
    }

    public void setAuctioneer(Player auctioneer) {
        this.auctioneer = auctioneer;
    }

    public Player getBidder() {
        return bidder;
    }

    public void setBidder(Player bidder) {
        this.bidder = bidder;
    }

    public ScheduledFuture getUpdate() {
        return update;
    }

    public void setUpdate(ScheduledFuture update) {
        this.update = update;
    }

    public AtomicBoolean getIsSelling() {
        return isSelling;
    }

    public void setIsSelling(AtomicBoolean isSelling) {
        this.isSelling = isSelling;
    }
}
