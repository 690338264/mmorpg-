package com.function.item.service;

import com.database.entity.Player;
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
        playerModel.getBagModel().getItemMap().remove(index);
    }

    /**
     * 移除装备
     */
    public void removeEquip(int equipId, PlayerModel playerModel) {
        String[] equips = playerModel.getEquip().split(",");
        StringBuilder e = new StringBuilder();
        for (int i = 0; i < equips.length; i++) {
            int id = Integer.parseInt(equips[i]);
            e.append(equips[i]);
            e.append(",");
            if (id == equipId) {
                continue;
            }
        }
        Item item = new Item();
        item.setId(equipId);
        playerModel.getEquipMap().remove(item.getItemById().getType());
        e.deleteCharAt(e.length() - 1);
        Player newPlayer = new Player();
        newPlayer.setEquip(e.toString());
        playerService.updateEquip(newPlayer, playerModel);
    }

    /**
     * 找空插入物品
     */
    public void addItem(int itemId, PlayerModel playerModel, ChannelHandlerContext ctx) {
        Item item = new Item();
        item.setId(itemId);
        item.setNum(1);
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
        bagService.updateBag(playerModel);
    }

    public void wearEquipment(int index, PlayerModel playerModel, ChannelHandlerContext ctx) {
        Item item = playerModel.getBagModel().getItemMap().get(index);
        if (item.getItemById().getType() != 2) {
            ctx.writeAndFlush("该物品不可穿戴！\n");
            return;
        }
        playerModel.setAtk(playerModel.getAtk() + item.getItemById().getAtk());
        playerModel.setDef(playerModel.getDef() + item.getItemById().getDef());
        playerModel.setSpeed(playerModel.getSpeed() + item.getItemById().getSpeed());
        StringBuilder newEquip = new StringBuilder(playerModel.getEquip());
        if (!playerModel.getEquip().equals("") && playerModel.getEquip() != null) {
            newEquip.append(",");
        }
        newEquip.append(item.getId());
        Player newPlayer = new Player();
        newPlayer.setEquip(newEquip.toString());
        //playerModel.setEquip(newEquip.toString());
        removeItem(index, playerModel);
        bagService.updateBag(playerModel);
        playerService.updateEquip(newPlayer, playerModel);
        ctx.writeAndFlush("您已成功穿戴:[" + item.getItemById().getName() + "]\n");
    }


}
