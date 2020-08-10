package com.function.item.service;

import com.function.bag.model.BagModel;
import com.function.bag.service.BagService;
import com.function.item.model.Item;
import com.function.player.model.PlayerModel;
import com.function.player.service.PlayerData;
import com.function.scene.service.NotifyScene;
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
    public void removeItem(int index, int num, PlayerModel playerModel) {
        int all = playerModel.getBagModel().getItemMap().get(index).getNum();
        if (all < num) {
            StringBuilder wrongNum = new StringBuilder("丢弃失败！你没有足够数量的该物品，请重试\n");
            notifyScene.notifyPlayer(playerModel, wrongNum);
            return;
        } else if (all == num) {
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
    public void removeEquip(int equipPlace, PlayerModel playerModel, ChannelHandlerContext ctx) {
        Item item = playerModel.getEquipMap().get(equipPlace);
        changeAttr(-1, item, playerModel);
        addItem(item, playerModel);
        playerModel.getEquipMap().remove(item.getItemById().getSpace());
        playerData.updateEquip(playerModel);
        ctx.writeAndFlush("您已摘下[" + item.getItemById().getName() + "]\n");
    }

    /**
     * 得到物品
     */
    public void getItem(Item item, PlayerModel playerModel) {
        if (item.getItemById().getType() == 1) {
            Map<Integer, Item> p = playerModel.getBagModel().getItemMap();
            for (Integer index : p.keySet()) {
                if (item.getId().equals(p.get(index).getId()) && p.get(index).getNum() < 100) {
                    p.get(index).setNum(p.get(index).getNum() + 1);
                    bagService.updateBag(playerModel);
                    playerModel.getChannelHandlerContext().writeAndFlush("[" + p.get(index).getItemById().getName() + "]已放入背包\n");
                    return;
                }
            }
        }
        addItem(item, playerModel);
    }

    /**
     * 找空插入物品
     */
    public void addItem(Item item, PlayerModel playerModel) {
        item.setNum(1);
        BagModel bagModel = playerModel.getBagModel();

        for (int i = 0; i < bagModel.getVolume(); i++) {
            if (bagModel.getItemMap().get(i) == null) {
                bagModel.getItemMap().put(i, item);
                if (item.getItemId() == null) {
                    item.setItemId(playerModel.getRoleid() * 10000 + bagModel.getMaxid());
                    bagModel.setMaxid(bagModel.getMaxid() + 1);
                }
                bagService.updateBag(playerModel);
                StringBuilder put = new StringBuilder("[").append(item.getItemById().getName()).append("]已放入背包\n");
                notifyScene.notifyPlayer(playerModel, put);
                return;
            }
        }
        StringBuilder full = new StringBuilder("背包已满！\n");
        notifyScene.notifyPlayer(playerModel, full);
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
        removeItem(index, 1, playerModel);
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
            removeEquip(item.getItemById().getSpace(), playerModel, ctx);
        }
        changeAttr(1, item, playerModel);
        removeItem(index, 1, playerModel);
        playerData.updateEquip(playerModel);
        playerModel.getEquipMap().put(item.getItemById().getSpace(), item);
        ctx.writeAndFlush("您已成功穿戴:[" + item.getItemById().getName() + "]\n");
    }

    /**
     * 显示已穿戴装备
     */
    public void listEquip(PlayerModel playerModel, ChannelHandlerContext ctx) {
        ctx.writeAndFlush("您已穿戴:\n");
        for (Integer key : playerModel.getEquipMap().keySet()) {
            ctx.writeAndFlush(key + ":[" + playerModel.getEquipMap().get(key).getItemById().getName() +
                    "]磨损度:[" + playerModel.getEquipMap().get(key).getNowWear() + "]\n");
        }
    }
}
