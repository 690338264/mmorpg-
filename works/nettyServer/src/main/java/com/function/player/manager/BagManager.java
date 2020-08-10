package com.function.player.manager;

import com.database.entity.Bag;
import com.database.entity.BagExample;
import com.database.entity.Player;
import com.function.player.model.PlayerModel;
import org.springframework.stereotype.Component;

/**
 * @author Catherine
 * @create 2020-08-06 18:36
 */
@Component
public class BagManager implements InitManager {

    @Override
    public BagExample init(PlayerModel playerModel) {
        BagExample bagExample = new BagExample();
        BagExample.Criteria criteria = bagExample.createCriteria();
        criteria.andPlayeridEqualTo(playerModel.getRoleid());
        return bagExample;
    }

    public Bag newBag(Player player) {
        Bag bag = new Bag();
        bag.setPlayerid(player.getRoleid());
        bag.setVolume(36);
        bag.setMaxid(0);
        return bag;
    }
}
