package com.function.item.service;

import com.function.bag.model.BagModel;
import com.function.bag.service.BagService;
import com.function.item.model.Item;
import com.function.player.model.PlayerModel;
import com.function.player.service.PlayerService;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Catherine
 */
@Component
public class ItemService {

    @Autowired
    private BagService bagService;
    @Autowired
    private PlayerService playerService;

    /**
     * 移除背包中的物品
     */
    public void removeItem(int index, PlayerModel playerModel) {
        if (playerModel.getBagModel().getItemMap().get(index).getNum() == 1) {
            playerModel.getBagModel().getItemMap().remove(index);
        } else {
            playerModel.getBagModel().getItemMap().get(index).setNum(playerModel.getBagModel().getItemMap().get(index).getNum() - 1);
        }
        bagService.updateBag(playerModel);
    }

    /**
     * 装拆装备的人物属性变化
     */
    public void changeAttr(int sign, Item item, PlayerModel playerModel) {
        playerModel.setAtk(playerModel.getAtk() + item.getItemById().getAtk() * sign);
        playerModel.setDef(playerModel.getDef() + item.getItemById().getDef() * sign);
        playerModel.setSpeed(playerModel.getSpeed() + item.getItemById().getSpeed() * sign);
    }

    /**
     * 移除装备
     */
    public void removeEquip(int equipId, PlayerModel playerModel, ChannelHandlerContext ctx) {
        String[] equips = playerModel.getEquip().split(",");
        StringBuilder e = new StringBuilder();
        for (int i = 0; i < equips.length; i++) {
            int id = Integer.parseInt(equips[i]);
            if (id == equipId) {
                continue;
            }
            e.append(equips[i]);
            e.append(",");
        }
        if (e.length() > 0) {
            e.deleteCharAt(e.length() - 1);
        }
        Item item = new Item();
        item.setId(equipId);
        playerModel.setEquip(e.toString());
        playerService.updateEquip(playerModel);
        changeAttr(-1, item, playerModel);
        addItem(equipId, playerModel, ctx);
        playerModel.getEquipMap().remove(item.getItemById().getSpace());
        ctx.writeAndFlush("您已摘下[" + item.getItemById().getName() + "]\n");

    }

    /**
     * 找空插入物品
     */
    public void addItem(int itemId, PlayerModel playerModel, ChannelHandlerContext ctx) {
        Item item = new Item();
        item.setId(itemId);
        item.setNum(1);
        item.setNowWear(playerModel.getEquipMap().get(item.getItemById().getSpace()).getNowWear());
        BagModel bagModel = playerModel.getBagModel();
        for (int i = 0; i < bagModel.getVolume(); i++) {
            if (bagModel.getItemMap().get(i) == null) {
                bagModel.getItemMap().put(i, item);
                StringBuilder items = new StringBuilder(bagModel.getItem());
                if (items != null && !items.equals("")) {
                    items.append(",");
                }
                items.append(itemId);
                bagModel.setItem(items.toString());
                bagService.updateBag(playerModel);
                return;
            }
        }
        ctx.writeAndFlush("背包已满！\n");
    }

    /**
     * 使用药品
     */
    public void useItem(int index, PlayerModel playerModel, ChannelHandlerContext ctx) {
        Item item = playerModel.getBagModel().getItemMap().get(index);
        if (item.getItemById().getType() != 1) {
            ctx.writeAndFlush("该物品不可使用！\n");
            return;
        }
        int addHp = playerModel.getHp() + item.getItemById().getHp();
        playerModel.setHp(addHp < playerModel.getOriHp() ? addHp : playerModel.getOriHp());
        int addMp = playerModel.getMp() + item.getItemById().getMp();
        playerModel.setMp(addMp < playerModel.getOriMp() ? addMp : playerModel.getOriMp());
        ctx.writeAndFlush("您成功使用[" + item.getItemById().getName() + "]\n");
        //移除已使用物品(num)
        removeItem(index, playerModel);
    }

    /**
     * 穿戴装备
     */
    public void wearEquipment(int index, PlayerModel playerModel, ChannelHandlerContext ctx) {
        Item item = playerModel.getBagModel().getItemMap().get(index);
        if (item.getItemById().getType() != 2) {
            ctx.writeAndFlush("该物品不可穿戴！\n");
            return;
        }
        if (playerModel.getEquipMap().get(item.getItemById().getSpace()) != null) {
            removeEquip(playerModel.getEquipMap().get(item.getItemById().getSpace()).getId(), playerModel, ctx);
        }
        changeAttr(1, item, playerModel);
        StringBuilder newEquip = new StringBuilder(playerModel.getEquip());
        if (!playerModel.getEquip().equals("") && playerModel.getEquip() != null) {
            newEquip.append(",");
        }
        newEquip.append(item.getId());
        playerModel.setEquip(newEquip.toString());
        removeItem(index, playerModel);
        playerService.updateEquip(playerModel);
        playerModel.getEquipMap().put(item.getItemById().getSpace(), item);
        ctx.writeAndFlush("您已成功穿戴:[" + item.getItemById().getName() + "]\n");
    }

    /**
     * 显示已穿戴装备
     */
    public void listEquip(PlayerModel playerModel, ChannelHandlerContext ctx) {
        ctx.writeAndFlush("您已穿戴:\n");
        for (Integer key : playerModel.getEquipMap().keySet()) {
            ctx.writeAndFlush(playerModel.getEquipMap().get(key).getItemById().getId() +
                    ":[" + playerModel.getEquipMap().get(key).getItemById().getName() +
                    "]磨损度:[" + playerModel.getEquipMap().get(key).getNowWear() + "]\n");
        }
    }
}
