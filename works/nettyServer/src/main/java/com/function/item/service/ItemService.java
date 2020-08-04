package com.function.item.service;

import com.function.bag.service.BagService;
import com.function.item.model.Item;
import com.function.player.model.PlayerModel;
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
        //移除已使用物品
        playerModel.getBagModel().getItemMap().remove(index);
        bagService.updateBag(playerModel);
    }

    public void wearEquipment(int index, PlayerModel playerModel, ChannelHandlerContext ctx) {

    }


}
