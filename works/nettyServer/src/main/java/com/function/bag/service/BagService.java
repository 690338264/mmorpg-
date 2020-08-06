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

import java.util.*;

/**
 * @author Catherine
 */
@Component
public class BagService {
    @Autowired
    private BagMapper bagMapper;

    /**
     * 加载背包
     */
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
            item.setNowWear(item.getItemById().getWear());
            bagModel.getItemMap().put(i, item);
        }

        orderBag(playerModel, playerModel.getBagModel().getItemMap());
    }

    /**
     * 更新背包
     */
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

    /**
     * 列出背包里的东西
     */
    public void listBag(PlayerModel playerModel, ChannelHandlerContext ctx) {
        BagModel bagModel = playerModel.getBagModel();
        ctx.writeAndFlush("您背包里的物品有：\n");
        for (Integer index : bagModel.getItemMap().keySet()) {
            StringBuilder sb = new StringBuilder();
            sb.append("[").append(index).append("]").append(bagModel.getItemMap().get(index).getItemById().getName())
                    .append("[").append(bagModel.getItemMap().get(index).getNum()).append("]");
            if (bagModel.getItemMap().get(index).getItemById().getType() == 2) {
                sb.append("磨损度:[").append(bagModel.getItemMap().get(index).getNowWear()).append("]");
            }
            sb.append('\n');
            ctx.writeAndFlush(sb);
        }
    }

    /**
     * 整理背包
     */
    public void orderBag(PlayerModel playerModel, Map<Integer, Item> itemMap) {
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
        playerModel.getBagModel().setItemMap(items);
    }

}
