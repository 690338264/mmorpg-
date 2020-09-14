package com.event;

import com.function.player.model.Player;

/**
 * @author Catherine
 * @create 2020-09-10 19:33
 */
public interface EventHandler<E extends QuestEvent> {
    /**
     * 事件管理
     *
     * @param event  事件
     * @param player 玩家
     */
    void handle(E event, Player player);
}
