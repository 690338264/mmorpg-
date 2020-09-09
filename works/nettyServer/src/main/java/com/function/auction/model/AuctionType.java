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
    Integer type;

    AuctionType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }
}
