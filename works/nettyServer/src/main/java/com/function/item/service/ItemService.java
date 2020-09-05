package com.function.item.service;

import com.function.bag.model.Bag;
import com.function.bag.service.BagService;
import com.function.item.model.Item;
import com.function.item.model.ItemType;
import com.function.player.model.Player;
import com.function.player.service.PlayerData;
import com.function.scene.service.NotifyScene;
import com.jpa.entity.TBag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Catherine
 */
@Component
public class ItemService {

    @Autowired
    private ItemEffect itemEffect;
    @Autowired
    private BagService bagService;
    @Autowired
    private PlayerData playerData;
    @Autowired
    private NotifyScene notifyScene;

    /**
     * 移除背包中的物品
     */
    public boolean removeItem(Long id, int index, int num, Player player) {

        if (!player.getBag().getItemMap().get(index).getItemId().equals(id)) {
            notifyScene.notifyPlayer(player, "失败！\n");
            return false;
        }
        int all = player.getBag().getItemMap().get(index).getNum();
        if (all < num) {
            StringBuilder wrongNum = new StringBuilder("失败！你没有足够数量的该物品，请重试\n");
            notifyScene.notifyPlayer(player, wrongNum);
            return false;
        } else if (all == num) {
            player.getBag().getItemMap().remove(index);
        } else {
            int number = player.getBag().getItemMap().get(index).getNum();
            player.getBag().getItemMap().get(index).setNum(number - num);
        }
        bagService.updateBag(player);
        return true;
    }

    /**
     * 装拆装备的人物属性变化
     *
     * @param sign 加/减属性
     */
    public void changeAttr(int sign, Item item, Player player) {
        player.setAtk(player.getAtk() + item.getItemById().getAtk() * sign);
        player.setDef(player.getDef() + item.getItemById().getDef() * sign);
        player.setSpeed(player.getSpeed() + item.getItemById().getSpeed() * sign);
    }

    /**
     * 移除装备
     */
    public void removeEquip(int equipPlace, Player player) {
        Item item = player.getEquipMap().get(equipPlace);
        changeAttr(-1, item, player);
        addItem(item, player);
        player.getEquipMap().remove(item.getItemById().getSpace());
        playerData.updatePlayer(player);
        notifyScene.notifyPlayer(player, MessageFormat.format("您已摘下[{0}]\n", item.getItemById().getName()));
    }

    /**
     * 得到物品
     */
    public boolean getItem(Item item, Player player) {
        int num = item.getNum();
        if (item.getItemById().getType() == ItemType.MEDICINAL.getType()) {
            Map<Integer, Item> p = player.getBag().getItemMap();
            for (Item value : p.values()) {
                if (item.getId().equals(value.getId()) && value.getNum() + num <= item.getMaxNum()) {
                    value.setNum(value.getNum() + num);
                    bagService.updateBag(player);
                    StringBuilder put = new StringBuilder("[")
                            .append(value.getItemById().getName()).append("]已放入背包\n");
                    notifyScene.notifyPlayer(player, put);
                    return true;
                }
            }
        }
        return addItem(item, player);
    }

    /**
     * 找空插入物品
     */
    public boolean addItem(Item item, Player player) {
        Bag bag = player.getBag();
        TBag tBag = bag.getTBag();
        for (int i = 0; i < tBag.getVolume(); i++) {
            if (bag.getItemMap().get(i) == null) {
                bag.getItemMap().put(i, item);
                if (item.getItemId() == null) {
                    //设置物品唯一id
                    item.setItemId(player.getTPlayer().getRoleId() * 1000000 + tBag.getMaxId());
                    tBag.setMaxId(tBag.getMaxId() + 1);
                }
                bagService.updateBag(player);
                StringBuilder put = new StringBuilder("[").append(item.getItemById().getName()).append("]已放入背包\n");
                notifyScene.notifyPlayer(player, put);
                return true;
            }
        }
        StringBuilder full = new StringBuilder("背包已满！\n");
        notifyScene.notifyPlayer(player, full);
        return false;
    }

    /**
     * 使用物品
     */
    public void useItem(int index, Player player) {
        Item item = player.getBag().getItemMap().get(index);
        removeItem(item.getItemId(), index, 1, player);
        itemEffect.useItem(player, item);
    }

    /**
     * 显示已穿戴装备
     */
    public void listEquip(Player player) {
        notifyScene.notifyPlayer(player, "您已穿戴:\n");
        player.getEquipMap().forEach((k, v) -> notifyScene.notifyPlayer(player, MessageFormat.format(
                "{0}:[{1}]磨损度:[{2}]\n",
                k, v.getItemById().getName(), v.getNowWear())));
    }

    /**
     * 修理装备
     */
    public void fixEquip(Player player, int index) {
        Item equip = player.getBag().getItemMap().get(index);
        if (equip.getItemById().getType() == ItemType.EQUIPMENT.getType()) {
            int oriWear = equip.getItemById().getWear();
            int money = (oriWear - equip.getNowWear()) * 50;
            if (subMoney(player, money)) {
                equip.setNowWear(oriWear);
                StringBuilder fix = new StringBuilder("您花费").append(money)
                        .append("金币修复装备[").append(equip.getItemById().getName()).append("]\n");
                notifyScene.notifyPlayer(player, fix);
            }
        }
    }


    public Item copyItem(Item item, int num) {
        Item copyItem = new Item(item.getId());
        copyItem.setNum(num);
        return copyItem;
    }

    public void getItem(Player player, List<Item> items) {
        Iterator<Item> iterator = items.iterator();
        while (iterator.hasNext()) {
            Item gift = iterator.next();
            if (!getItem(gift, player)) {
                return;
            }
            iterator.remove();
        }
    }

    /**
     * 扣除金币
     */
    public boolean subMoney(Player player, int money) {
        if (money < 0) {
            notifyScene.notifyPlayer(player, "数量非法！\n");
            return false;
        }
        int remain = player.getTPlayer().getMoney() - money;
        if (remain < 0) {
            notifyScene.notifyPlayer(player, "失败！金币不足\n");
            return false;
        } else {
            player.getTPlayer().setMoney(remain);
            playerData.updatePlayer(player);
            return true;
        }
    }
}
