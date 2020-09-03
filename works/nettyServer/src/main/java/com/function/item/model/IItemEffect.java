package com.function.item.model;

import com.function.player.model.Player;

/**
 * @author Catherine
 * @create 2020-09-03 16:52
 */
public interface IItemEffect {
    /**
     * 不同药品效果
     *
     * @param player 使用玩家
     * @param item   使用物品
     */
    void itemEffect(Player player, Item item);
}
