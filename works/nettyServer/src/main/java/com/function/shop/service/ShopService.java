package com.function.shop.service;

import com.function.item.excel.ItemExcel;
import com.function.item.excel.ItemResource;
import com.function.item.model.Item;
import com.function.item.service.ItemService;
import com.function.player.model.Player;
import com.function.scene.service.NotifyScene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

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

    public void show(Player player) {
        StringBuilder good = new StringBuilder("商店:\n");
        for (Integer key : ItemResource.getMap().keySet()) {
            ItemExcel item = ItemResource.getMap().get(key);
            good.append(key).append(item.getName())
                    .append(":").append(item.getMoney()).append('\n');
        }
        notifyScene.notifyPlayer(player, good);
    }

    public void buy(Player player, int itemId, int num) {
        int money = ItemResource.getItemById(itemId).getMoney() * num;
        if (itemService.subMoney(player, money)) {
            IntStream.range(0, num).forEach(i -> {
                        Item item = new Item();
                        item.setId(itemId);
                        itemService.getItem(item, player);
                    }
            );
            StringBuilder success = new StringBuilder("购买成功！   花费:").append(money).append("金币\n");
            notifyScene.notifyPlayer(player, success);
        }

    }

    public void sell(Player player, int index, int num) {
        Item sellItem = player.getBag().getItemMap().get(index);
        int total = sellItem.getNum();
        if (total < num) {
            StringBuilder fail = new StringBuilder("您没有足够多的物品用来卖\n");
            notifyScene.notifyPlayer(player, fail);
            return;
        }
        if (sellItem.getItemById().getType() == 2) {
            itemService.fixEquip(player, index);
        }
        itemService.removeItem(index, num, player);
        int getMoney = sellItem.getItemById().getMoney() * num;
        int orgMoney = player.getTPlayer().getMoney();
        player.getTPlayer().setMoney(orgMoney + getMoney);
        StringBuilder success = new StringBuilder("出售成功！   获得金币：").append(getMoney).append('\n');
        notifyScene.notifyPlayer(player, success);
    }


}