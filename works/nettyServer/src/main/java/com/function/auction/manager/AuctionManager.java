package com.function.auction.manager;

import com.function.auction.model.Auction;
import com.function.auction.model.AuctionType;
import com.function.auction.service.AuctionService;
import com.jpa.dao.AuctionDAO;
import com.jpa.entity.TAuction;
import com.jpa.manager.JpaManager;
import com.manager.ThreadPoolManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * @author Catherine
 * @create 2020-09-08 12:29
 */
@Component
public class AuctionManager {
    @Autowired
    private AuctionService auctionService;
    @Autowired
    private AuctionDAO auctionDAO;
    @Autowired
    private JpaManager jpaManager;
    private final Map<Long, Auction> fixedPriceMode = new ConcurrentHashMap<>();
    private final Map<Long, Auction> auctionMode = new ConcurrentHashMap<>();
    private static final long JUMP = 1000;

    @PostConstruct
    private void init() {
        auctionDAO.findAll().forEach(tAuction -> {
            Auction auction = new Auction(tAuction);
            auction.fromJson();
            putAuction(auction);
        });
        ThreadPoolManager.loopThread(() -> {
            fixedPriceMode.forEach((id, auction) -> {
                if (System.currentTimeMillis() > auction.gettAuction().getFinishTime()) {
                    auctionService.cancelAuction(auction);
                }
            });
            auctionMode.forEach((id, auction) -> {
                if (System.currentTimeMillis() > auction.gettAuction().getFinishTime()) {
                    if (auction.gettAuction().getBidder() == null) {
                        auctionService.cancelAuction(auction);
                    } else {
                        auctionService.finishCompetition(auction);
                    }
                }
            });
        }, 0, JUMP, getClass().hashCode());
    }

    /**
     * 把拍卖添加进Map
     */
    public void putAuction(Auction auction) {
        TAuction tAuction = auction.gettAuction();
        if (tAuction.getType() == AuctionType.FIXED_PRICE.getType()) {
            fixedPriceMode.put(tAuction.getAuctionId(), auction);
        } else {
            auctionMode.put(tAuction.getAuctionId(), auction);
        }
    }

    public void updateAuction(Auction auction) {
        ScheduledFuture<?> update = jpaManager.update(auction.getUpdate(), () -> {
            auction.toJson();
            auctionDAO.save(auction.gettAuction());
            auction.setUpdate(null);
        }, auction.gettAuction().getAuctionId().intValue());
        auction.setUpdate(update);
    }

    public Map<Long, Auction> getFixedPriceMode() {
        return fixedPriceMode;
    }

    public Map<Long, Auction> getAuctionMode() {
        return auctionMode;
    }
}