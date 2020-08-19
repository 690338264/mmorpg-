package com.function.item.service;

import com.function.bag.model.Bag;
import com.function.bag.service.BagService;
import com.function.item.model.Item;
import com.function.player.model.Player;
import com.function.player.service.PlayerData;
import com.function.scene.service.NotifyScene;
import com.jpa.entity.TBag;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Catherine
 */
@Component
public class ItemService {

    @Autowired
    private BagService bagService;
    @Autowired
    private PlayerData playerData;
    @Autowired
    private NotifyScene notifyScene;

    /**
     * 移除背包中的物品
     */
    public void removeItem(int index, int num, Player player) {
        int all = player.getBag().getItemMap().get(index).getNum();
        if (all < num) {
            StringBuilder wrongNum = new StringBuilder("丢弃失败！你没有足够数量的该物品，请重试\n");
            notifyScene.notifyPlayer(player, wrongNum);
            return;
        } else if (all == num) {
            player.getBag().getItemMap().remove(index);
        } else {
            int number = player.getBag().getItemMap().get(index).getNum();
            player.getBag().getItemMap().get(index).setNum(number - num);
        }
        bagService.updateBag(player);
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
    public void removeEquip(int equipPlace, Player player, ChannelHandlerContext ctx) {
        Item item = player.getEquipMap().get(equipPlace);
        changeAttr(-1, item, player);
        addItem(item, player);
        player.getEquipMap().remove(item.getItemById().getSpace());
        playerData.updateEquip(player);
        ctx.writeAndFlush("您已摘下[" + item.getItemById().getName() + "]\n");
    }

    /**
     * 得到物品
     */
    public void getItem(Item item, Player player) {
        if (item.getItemById().getType() == 1) {
            Map<Integer, Item> p = player.getBag().getItemMap();
            for (Integer index : p.keySet()) {
                if (item.getId().equals(p.get(index).getId()) && p.get(index).getNum() < 100) {
                    p.get(index).setNum(p.get(index).getNum() + 1);
                    bagService.updateBag(player);
                    StringBuilder put = new StringBuilder("[")
                            .append(p.get(index).getItemById().getName()).append("]已放入背包\n");
                    notifyScene.notifyPlayer(player, put);
                    return;
                }
            }
        }
        addItem(item, player);
    }

    /**
     * 找空插入物品
     */
    public void addItem(Item item, Player player) {
        item.setNum(1);
        Bag bag = player.getBag();
        TBag tBag = bag.getTBag();
        for (int i = 0; i < tBag.getVolume(); i++) {
            if (bag.getItemMap().get(i) == null) {
                bag.getItemMap().put(i, item);
                if (item.getItemId() == null) {
                    item.setItemId(player.getTPlayer().getRoleId() * 10000 + tBag.getMaxId());
                    tBag.setMaxId(tBag.getMaxId() + 1);
                }
                bagService.updateBag(player);
                StringBuilder put = new StringBuilder("[").append(item.getItemById().getName()).append("]已放入背包\n");
                notifyScene.notifyPlayer(player, put);
                return;
            }
        }
        StringBuilder full = new StringBuilder("背包已满！\n");
        notifyScene.notifyPlayer(player, full);
    }

    /**
     * 使用药品
     */
    public void useItem(int index, Player player, ChannelHandlerContext ctx) {
        Item item = player.getBag().getItemMap().get(index);
        if (item.getItemById().getType() != 1) {
            ctx.writeAndFlush("该物品不可使用！\n");
            return;
        }
        synchronized (this) {
            removeItem(index, 1, player);
            int addHp = player.getHp() + item.getItemById().getHp();
            player.setHp(addHp < player.getOriHp() ? addHp : player.getOriHp());
            int addMp = player.getMp() + item.getItemById().getMp();
            player.setMp(addMp < player.getOriMp() ? addMp : player.getOriMp());
            ctx.writeAndFlush("您成功使用[" + item.getItemById().getName() + "]\n");
        }
    }

    /**
     * 穿戴装备
     */
    public void wearEquipment(int index, Player player, ChannelHandlerContext ctx) {
        Item item = player.getBag().getItemMap().get(index);
        if (item.getItemById().getType() != 2) {
            ctx.writeAndFlush("该物品不可穿戴！\n");
            return;
        }
        removeItem(index, 1, player);
        if (player.getEquipMap().get(item.getItemById().getSpace()) != null) {
            removeEquip(item.getItemById().getSpace(), player, ctx);
        }
        changeAttr(1, item, player);

        player.getEquipMap().put(item.getItemById().getSpace(), item);
        playerData.updateEquip(player);

        ctx.writeAndFlush("您已成功穿戴:[" + item.getItemById().getName() + "]\n");
    }

    /**
     * 显示已穿戴装备
     */
    public void listEquip(Player player, ChannelHandlerContext ctx) {
        ctx.writeAndFlush("您已穿戴:\n");
        for (Integer key : player.getEquipMap().keySet()) {
            ctx.writeAndFlush(key + ":[" + player.getEquipMap().get(key).getItemById().getName() +
                    "]磨损度:[" + player.getEquipMap().get(key).getNowWear() + "]\n");
        }
    }

    /**
     * 修理装备
     */
    public void fixEquip(Player player, int index) {
        Item equip = player.getBag().getItemMap().get(index);
        if (equip.getItemById().getType() == 2) {
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

    public boolean subMoney(Player player, int money) {
        int remain = player.getTPlayer().getMoney() - money;
        if (remain < 0) {
            StringBuilder fail = new StringBuilder("失败！金币不足\n");
            notifyScene.notifyPlayer(player, fail);
            return false;
        } else {
            player.getTPlayer().setMoney(remain);
            return true;
        }
    }
}
