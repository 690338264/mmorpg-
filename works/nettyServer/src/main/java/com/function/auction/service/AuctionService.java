package com.function.auction.service;

import com.function.auction.manager.AuctionManager;
import com.function.auction.model.Auction;
import com.function.auction.model.AuctionType;
import com.function.item.model.Item;
import com.function.item.service.ItemService;
import com.function.player.model.Player;
import com.function.player.service.PlayerData;
import com.function.scene.service.NotifyScene;
import com.jpa.dao.AuctionDAO;
import com.jpa.entity.TAuction;
import com.manager.ThreadPoolManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

/**
 * @author Catherine
 * @create 2020-09-08 15:12
 */
@Component
public class AuctionService {
    @Autowired
    private NotifyScene notifyScene;
    @Autowired
    private ItemService itemService;
    @Autowired
    private AuctionDAO auctionDAO;
    @Autowired
    private AuctionManager auctionManager;
    @Autowired
    private PlayerData playerData;

    private final static float FEE = 0.80f;

    /**
     * 查看一口价列表
     */
    public void showFixedPrice(Player player) {
        StringBuilder content = new StringBuilder("一口价拍卖商城:\n");
        auctionManager.getFixedPriceMode().forEach((auctionId, auction) ->
                content.append(MessageFormat.format("{0}:{1}[{2}]  售价:{3}\n",
                        auctionId, auction.getItem().getItemById().getName(), auction.getItem().getNum(),
                        auction.gettAuction().getHighestMoney())));
        notifyScene.notifyPlayer(player, content);
    }

    /**
     * 查看竞价拍卖
     */
    public void showCompetitionAuction(Player player) {
        StringBuilder content = new StringBuilder("竞价拍卖商城\n");
        auctionManager.getAuctionMode().forEach((auctionId, auction) ->
                content.append(MessageFormat.format("{0}:{1}[{2}]  最高价:{3}\n",
                        auctionId, auction.getItem().getItemById().getName(), auction.getItem().getNum(),
                        auction.gettAuction().getHighestMoney()))
        );
        notifyScene.notifyPlayer(player, content);
    }

    /**
     * 新建拍卖
     */
    public void createAuction(Player player, int type, int index, int num, int money, long time) {
        Item item = player.getBag().getItemMap().get(index);
        if (!itemService.removeItem(item.getItemId(), index, num, player)) {
            return;
        }
        TAuction tAuction = new TAuction();
        tAuction.setType(type);
        tAuction.setHighestMoney(money);
        tAuction.setFinishTime(System.currentTimeMillis() + time);
        auctionDAO.saveAndFlush(tAuction);

        Auction auction = new Auction(tAuction);
        auction.setAuctioneer(player);
        auction.setItem(itemService.takeOutItem(item, num));

        auctionManager.putAuction(auction);
        auctionManager.updateAuction(auction);
        notifyScene.notifyPlayer(player, "物品上架\n");
    }

    /**
     * 购买一口价商品
     */
    public void buyFixedAuction(Player player, long auctionId) {
        try {
            Auction auction = auctionManager.getFixedPriceMode().get(auctionId);
            int cost = auction.gettAuction().getHighestMoney();
            if (!auction.getIsSelling().get()) {
                notifyScene.notifyPlayer(player, "购买失败!\n");
                return;
            }
            auction.getIsSelling().set(true);
            if (!itemService.subMoney(player, cost)) {
                auction.getIsSelling().set(false);
                return;
            }
            auctionManager.getFixedPriceMode().remove(auctionId);

            beginChange(auction, player);
            playerData.updatePlayer(auction.getAuctioneer());
            auctionDAO.delete(auction.gettAuction());

            notifyScene.notifyPlayer(player, "购得拍卖品!\n");
        } catch (Exception e) {
            notifyScene.notifyPlayer(player, "拍卖品不存在!\n");
        }
    }

    /**
     * 参加竞拍
     */
    public void joinCompetition(Player player, long auctionId, int money) {
        try {
            Auction auction = auctionManager.getAuctionMode().get(auctionId);
            if (!auction.getIsSelling().get()) {
                auction.getIsSelling().set(true);
            } else {
                notifyScene.notifyPlayer(player, "出价失败\n");
                return;
            }
            int highestMoney = auction.gettAuction().getHighestMoney();
            if (money <= highestMoney || !itemService.subMoney(player, money)) {
                notifyScene.notifyPlayer(player, "出价失败\n");
                auction.getIsSelling().set(false);
                return;
            }
            if (auction.getBidder() != null) {
                Player bidder = auction.getBidder();
                ThreadPoolManager.immediateThread(() ->
                                bidder.getTPlayer().setMoney(bidder.getTPlayer().getMoney() + highestMoney),
                        bidder.getTPlayer().getRoleId().intValue());
                playerData.updatePlayer(bidder);
            }
            auction.setBidder(player);
            auction.gettAuction().setHighestMoney(money);
            auction.getIsSelling().set(false);
            auctionManager.updateAuction(auction);
            playerData.updatePlayer(player);
            notifyScene.notifyPlayer(player, "出价成功!\n");
        } catch (Exception e) {
            notifyScene.notifyPlayer(player, "拍卖品不存在！\n");
        }
    }

    public void finishCompetition(Auction auction) {
        Player bidder = auction.getBidder();
        ThreadPoolManager.immediateThread(() ->
                        beginChange(auction, bidder),
                bidder.getTPlayer().getRoleId().intValue()
        );
        playerData.updatePlayer(auction.getAuctioneer());
        auctionManager.getAuctionMode().remove(auction.gettAuction().getAuctionId());
        auctionDAO.delete(auction.gettAuction());

    }

    /**
     * 开始物品交换
     */
    public void beginChange(Auction auction, Player bidder) {
        itemService.getItem(auction.getItem(), bidder);
        int money = auction.getAuctioneer().getTPlayer().getMoney();
        Player auctioneer = auction.getAuctioneer();
        ThreadPoolManager.immediateThread(() ->
                        auctioneer.getTPlayer().setMoney((int) (money + auction.gettAuction().getHighestMoney() * FEE)),
                auctioneer.getTPlayer().getRoleId().intValue()
        );
    }

    /**
     * 取消拍卖  商品回退
     */
    public void cancelAuction(Auction auction) {
        if (auction.getIsSelling().get()) {
            return;
        }
        auction.getIsSelling().set(true);
        Item item = auction.getItem();
        Player auctioneer = auction.getAuctioneer();

        ThreadPoolManager.immediateThread(() ->
                itemService.getItem(item, auctioneer), auctioneer.getTPlayer().getRoleId().intValue()
        );

        if (auction.gettAuction().getType() == AuctionType.FIXED_PRICE.getType()) {
            auctionManager.getFixedPriceMode().remove(auction.gettAuction().getAuctionId());
        } else {
            auctionManager.getAuctionMode().remove(auction.gettAuction().getAuctionId());
        }
        auctionDAO.delete(auction.gettAuction());
        notifyScene.notifyPlayer(auctioneer, "物品未售出\n");
    }

}
