package com.function.bag.service;

import com.alibaba.fastjson.JSON;
import com.function.bag.model.Bag;
import com.function.item.model.Item;
import com.function.player.model.Player;
import com.jpa.dao.BagDAO;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author Catherine
 */
@Component
public class BagService {
    @Autowired
    private BagDAO bagDAO;

    /**
     * 更新背包
     */
    public void updateBag(Player player) {
        String json = JSON.toJSONString(player.getBag().getItemMap());
        player.getBag().getTBag().setItem(json);
        bagDAO.save(player.getBag().getTBag());
    }

    /**
     * 列出背包里的东西
     */
    public void listBag(Player player, ChannelHandlerContext ctx) {
        Bag bag = player.getBag();
        StringBuilder sb = new StringBuilder("您背包里的物品有：\n");

        for (Integer index : bag.getItemMap().keySet()) {
            Item item = bag.getItemMap().get(index);
            sb.append("[").append(index).append("]").append(item.getItemById().getName())
                    .append("[").append(item.getNum()).append("]");
            if (item.getItemById().getType() == 2) {
                sb.append("磨损度:[").append(item.getNowWear()).append("]");
            }
            sb.append('\n');

        }
        sb.append("金币：").append(player.getTPlayer().getMoney()).append('\n');
        ctx.writeAndFlush(sb);
    }

    /**
     * 整理背包
     */
    public void orderBag(Player player, Map<Integer, Item> itemMap) {
        List<Map.Entry<Integer, Item>> list = new LinkedList<>(itemMap.entrySet());
        Collections.sort(list, (o1, o2) -> {
            int compare = (o1.getValue().getId()).compareTo(o2.getValue().getId());
            return compare;
        });
        Map<Integer, Item> result = new HashMap<>();
        int key = 0;
        for (Map.Entry<Integer, Item> entry : list) {
            result.put(key, entry.getValue());
            key++;
        }

        int index = 0;
        Map<Integer, Item> items = new HashMap<>();
        for (Integer i : result.keySet()) {
            if (i < result.size() - 1) {
                if (result.get(i + 1).getId().equals(result.get(i).getId()) && result.get(i).getItemById().getType() == 1) {
                    result.get(i + 1).setNum(result.get(i + 1).getNum() + result.get(i).getNum());
                } else {
                    items.put(index, result.get(i));
                    index++;
                }
            } else {
                items.put(index, result.get(i));
            }
        }
        player.getBag().setItemMap(items);
        updateBag(player);
    }

}
