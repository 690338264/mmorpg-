package com.function.bag.service;

import com.alibaba.fastjson.JSON;
import com.database.entity.Bag;
import com.database.entity.BagExample;
import com.database.mapper.BagMapper;
import com.function.bag.model.BagModel;
import com.function.item.model.Item;
import com.function.player.manager.BagManager;
import com.function.player.manager.InitManager;
import com.function.player.model.PlayerModel;
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
    private BagMapper bagMapper;
    @Autowired
    private BagManager bagManager;

    InitManager initManager;

    /**
     * 更新背包
     */
    public void updateBag(PlayerModel playerModel) {
        setInitManager(bagManager);
        BagExample bagExample = (BagExample) initData(playerModel);
        Bag newBag = new Bag();
        String json = JSON.toJSONString(playerModel.getBagModel().getItemMap());
        playerModel.getBagModel().setItem(json);
        newBag.setItem(json);
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
        updateBag(playerModel);
    }

    public void setInitManager(InitManager initManager) {
        this.initManager = initManager;
    }

    public Object initData(PlayerModel playerModel) {
        return initManager.init(playerModel);
    }

}
