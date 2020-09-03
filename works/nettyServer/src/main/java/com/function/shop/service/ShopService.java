package com.function.shop.service;

import com.function.item.excel.ItemExcel;
import com.function.item.excel.ItemResource;
import com.function.item.model.Item;
import com.function.item.model.ItemType;
import com.function.item.service.ItemService;
import com.function.player.model.Player;
import com.function.player.service.PlayerData;
import com.function.scene.service.NotifyScene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

/**
 * @author Catherine
 * @create 2020-08-19 16:10
 */
@Component
public class ShopService {
    @Autowired
    private NotifyScene notifyScene;
    @Autowired
    private ItemService itemService;
    @Autowired
    private PlayerData playerData;

    /**
     * 显示商店
     */
    public void show(Player player) {
        StringBuilder good = new StringBuilder("商店:\n");
        for (Integer key : ItemResource.getMap().keySet()) {
            ItemExcel item = ItemResource.getMap().get(key);
            good.append(key).append(item.getName())
                    .append(":").append(item.getMoney()).append('\n');
        }
        notifyScene.notifyPlayer(player, good);
    }

    /**
     * 买东西
     */
    public void buy(Player player, int itemId, int num) {
        int money = 0;
        int remain = num;
        while (true) {
            Item item = new Item();
            item.setId(itemId);
            num = Math.min(remain, item.getMaxNum());
            item.setNum(num);
            int part = ItemResource.getItemById(itemId).getMoney() * num;
            remain = remain - num;
            if (itemService.getItem(item, player) && itemService.subMoney(player, part)) {
                money = money + part;
                if (remain == 0) {
                    notifyScene.notifyPlayer(player, MessageFormat.format("购买成功！   花费:{0}金币\n", money));
                    return;
                }
            } else {
                notifyScene.notifyPlayer(player, "购买失败！\n");
                return;
            }
        }

    }

    /**
     * 卖东西
     */
    public void sell(Player player, int index, int num) {
        Item sellItem = player.getBag().getItemMap().get(index);
        int total = sellItem.getNum();
        if (total < num) {
            StringBuilder fail = new StringBuilder("您没有足够多的物品用来卖\n");
            notifyScene.notifyPlayer(player, fail);
            return;
        }
        if (sellItem.getItemById().getType() == ItemType.EQUIPMENT.getType()) {
            itemService.fixEquip(player, index);
        }
        itemService.removeItem(sellItem.getItemId(), index, num, player);
        int getMoney = sellItem.getItemById().getMoney() * num;
        int orgMoney = player.getTPlayer().getMoney();
        player.getTPlayer().setMoney(orgMoney + getMoney);
        playerData.updatePlayerInfo(player);
        StringBuilder success = new StringBuilder("出售成功！   获得金币：").append(getMoney).append('\n');
        notifyScene.notifyPlayer(player, success);
    }


}