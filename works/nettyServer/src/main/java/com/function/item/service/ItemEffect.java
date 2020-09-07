package com.function.item.service;

import com.function.item.model.IItemEffect;
import com.function.item.model.Item;
import com.function.item.model.ItemType;
import com.function.player.model.Player;
import com.function.player.service.PlayerData;
import com.function.scene.service.NotifyScene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Catherine
 * @create 2020-09-03 16:54
 */
@Component
public class ItemEffect {
    @Autowired
    private NotifyScene notifyScene;
    @Autowired
    private ItemService itemService;
    @Autowired
    private PlayerData playerData;

    private final Map<Integer, IItemEffect> itemEffectMap = new HashMap<>();

    {
        itemEffectMap.put(ItemType.MEDICINAL.getType(), this::drink);
        itemEffectMap.put(ItemType.EQUIPMENT.getType(), this::putOn);

    }

    public void useItem(Player player, Item item) {
        itemEffectMap.get(item.getItemById().getType()).itemEffect(player, item);
    }

    /**
     * 使用药品
     */
    public void drink(Player player, Item item) {
        int addHp = player.getHp() + item.getItemById().getHp();
        player.setHp(Math.min(addHp, player.getOriHp()));
        int addMp = player.getMp() + item.getItemById().getMp();
        player.setMp(addMp < player.getOriMp() ? addMp : player.getOriMp());
        notifyScene.notifyPlayer(player, MessageFormat.format("您成功使用[{0}]\n", item.getItemById().getName()));
    }

    /**
     * 穿戴装备
     */
    public void putOn(Player player, Item item) {
        if (player.getEquipMap().get(item.getItemById().getSpace()) != null) {
            itemService.removeEquip(item.getItemById().getSpace(), player);
        }
        itemService.changeAttr(1, item, player);
        player.getEquipMap().put(item.getItemById().getSpace(), item);
        playerData.updatePlayer(player);
        notifyScene.notifyPlayer(player, MessageFormat.format("您已成功穿戴:[{0}]\n", item.getItemById().getName()));
    }

}
