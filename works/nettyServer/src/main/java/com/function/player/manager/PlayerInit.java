package com.function.player.manager;

import com.database.entity.PlayerExample;
import com.function.player.model.PlayerModel;

/**
 * @author Catherine
 * @create 2020-08-06 18:44
 */
public class PlayerInit implements InitManager {
    @Override
    public PlayerExample init(PlayerModel playerModel) {
        PlayerExample playerExample = new PlayerExample();
        PlayerExample.Criteria criteria = playerExample.createCriteria();
        criteria.andRoleidEqualTo(playerModel.getRoleid());
        return playerExample;
    }
}
