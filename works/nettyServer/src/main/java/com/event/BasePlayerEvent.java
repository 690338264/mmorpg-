package com.event;

import com.function.player.model.Player;

/**
 * @author Catherine
 * @create 2020-09-16 22:33
 */
public abstract class BasePlayerEvent implements IEvent {
    private Player player;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
