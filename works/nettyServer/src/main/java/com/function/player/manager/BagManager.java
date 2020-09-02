package com.function.player.manager;

import com.jpa.entity.TBag;
import com.jpa.entity.TPlayer;
import org.springframework.stereotype.Component;

/**
 * @author Catherine
 * @create 2020-08-06 18:36
 */
@Component
public class BagManager {

    public TBag newBag(TPlayer player) {
        TBag bag = new TBag();
        bag.setPlayerId(player.getRoleId());
        bag.setVolume(36);
        bag.setMaxId(0L);
        bag.setItem("{}");
        return bag;
    }
}
