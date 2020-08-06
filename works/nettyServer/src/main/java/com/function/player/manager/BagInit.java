package com.function.player.manager;

import com.database.entity.BagExample;
import com.function.player.model.PlayerModel;

/**
 * @author Catherine
 * @create 2020-08-06 18:36
 */
public class BagInit implements InitManager {

    @Override
    public BagExample init(PlayerModel playerModel) {
        BagExample bagExample = new BagExample();
        BagExample.Criteria criteria = bagExample.createCriteria();
        criteria.andPlayeridEqualTo(playerModel.getRoleid());
        return bagExample;
    }
}
