package com.function.auction.model;

/**
 * @author Catherine
 * @create 2020-09-08 12:49
 */
public enum AuctionType {
    //
    FIXED_PRICE(1),

    COMPETITION_PRICE(2),
    ;
    int type;

    AuctionType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
