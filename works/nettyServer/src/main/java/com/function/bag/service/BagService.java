package com.function.bag.service;

import com.database.entity.Bag;
import com.database.entity.BagExample;
import com.database.mapper.BagMapper;
import com.function.bag.model.BagModel;
import com.function.item.model.Item;
import com.function.player.model.PlayerModel;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BagService {
    @Autowired
    private BagMapper bagMapper;

    public void initBag(PlayerModel playerModel) {
        BagExample bagExample = new BagExample();
        BagExample.Criteria criteria = bagExample.createCriteria();
        criteria.andPlayeridEqualTo(playerModel.getRoleid());
        List<Bag> bagList = bagMapper.selectByExample(bagExample);
        BagModel bagModel = new BagModel();
        BeanUtils.copyProperties(bagList.get(0), bagModel);
        playerModel.setBagModel(bagModel);
        if (bagModel.getItem().equals("") && bagModel.getItem() != null) {
            return;
        }
        String[] items = bagModel.getItem().split(",");
        for (int i = 0; i < items.length; i++) {
            int itemId = Integer.parseInt(items[i]);
            Item item = new Item();
            item.setId(itemId);
            item.setNum(1);
            bagModel.getItemMap().put(i, item);
        }
    }

    public void updateBag(PlayerModel playerModel) {
        BagExample bagExample = new BagExample();
        BagExample.Criteria criteria = bagExample.createCriteria();
        criteria.andPlayeridEqualTo(playerModel.getRoleid());
        Bag newBag = new Bag();
        StringBuilder item = new StringBuilder();
        for (Integer index : playerModel.getBagModel().getItemMap().keySet()) {
            int num = playerModel.getBagModel().getItemMap().get(index).getNum();
            for (int i = 0; i < num; i++) {
                item.append(playerModel.getBagModel().getItemMap().get(index).getId());
                item.append(",");
            }
        }
        //去掉最后一个,
        item.deleteCharAt(item.length() - 1);
        newBag.setItem(item.toString());
        bagMapper.updateByExampleSelective(newBag, bagExample);
    }

    public void listBag(PlayerModel playerModel, ChannelHandlerContext ctx) {
        BagModel bagModel = playerModel.getBagModel();
        ctx.writeAndFlush("您背包里的物品有：\n");
        for (Integer index : bagModel.getItemMap().keySet()) {
            ctx.writeAndFlush("[" + index + "]" + bagModel.getItemMap().get(index).getItemById().getName() + '\n');
        }
    }

}
