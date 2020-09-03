package com.function.bag.service;

import com.alibaba.fastjson.JSON;
import com.function.bag.model.Bag;
import com.function.item.model.Item;
import com.function.item.model.ItemType;
import com.function.player.model.Player;
import com.function.player.model.SceneObjectTask;
import com.function.scene.service.NotifyScene;
import com.jpa.dao.BagDAO;
import com.manager.ThreadPoolManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ScheduledFuture;

/**
 * @author Catherine
 */
@Component
public class BagService {
    @Autowired
    private BagDAO bagDAO;
    @Autowired
    private NotifyScene notifyScene;

    /**
     * 更新背包
     */
    public void updateBag(Player player) {
        int key = SceneObjectTask.UPDATE_BAG.getKey();
        if (player.getTaskMap().get(key) == null) {
            ScheduledFuture update = ThreadPoolManager.delayThread(() -> {
                String json = JSON.toJSONString(player.getBag().getItemMap());
                player.getBag().getTBag().setItem(json);
                bagDAO.save(player.getBag().getTBag());
                player.getTaskMap().remove(key);
            }, SceneObjectTask.UPDATE_TIME.getKey(), player.getTPlayer().getRoleId().intValue());
            player.getTaskMap().put(key, update);
        }
    }

    /**
     * 列出背包里的东西
     */
    public void listBag(Player player) {
        Bag bag = player.getBag();
        notifyScene.notifyPlayer(player, "背包里的物品有:\n");
        for (Integer index : bag.getItemMap().keySet()) {
            Item item = bag.getItemMap().get(index);
            notifyScene.notifyPlayer(player, MessageFormat.format("[{0}]{1}[{2}]",
                    index, item.getItemById().getName(), item.getNum()));
            if (item.getItemById().getType() == ItemType.EQUIPMENT.getType()) {
                notifyScene.notifyPlayer(player, MessageFormat.format("磨损度:[{0}],", item.getNowWear()));
            }
            notifyScene.notifyPlayer(player, MessageFormat.format("  id:{0}\n", item.getItemId()));
        }
        notifyScene.notifyPlayer(player, MessageFormat.format("金币：{0}\n", player.getTPlayer().getMoney()));
    }

    /**
     * 整理背包
     */
    public void orderBag(Player player, Map<Integer, Item> itemMap) {
        List<Map.Entry<Integer, Item>> list = new LinkedList<>(itemMap.entrySet());
        list.sort(Comparator.comparing(o -> (o.getValue().getId())));
        Map<Integer, Item> result = new HashMap<>();
        int key = 0;
        for (Map.Entry<Integer, Item> entry : list) {
            result.put(key, entry.getValue());
            key++;
        }

        int index = 0;
        Map<Integer, Item> items = new HashMap<>();
        for (Integer i : result.keySet()) {
            Item item = result.get(i);
            if (i < result.size() - 1) {
                Item nextItem = result.get(i + 1);
                if (nextItem.getId().equals(item.getId()) && item.getItemById().getType() == ItemType.MEDICINAL.getType()) {
                    if (nextItem.getNum() + item.getNum() > item.getMaxNum()) {
                        int num = nextItem.getNum() - item.getMaxNum() + item.getNum();
                        item.setNum(item.getMaxNum());
                        nextItem.setNum(num);
                        items.put(index, result.get(i));
                        index++;
                    } else {
                        nextItem.setNum(nextItem.getNum() + item.getNum());
                    }
                } else {
                    items.put(index, item);
                    index++;
                }
            } else {
                items.put(index, item);
            }
        }
        player.getBag().setItemMap(items);
        updateBag(player);
    }

}
