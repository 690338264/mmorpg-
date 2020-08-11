package com.function.player.manager;

import com.database.entity.Player;
import com.database.entity.PlayerExample;
import com.function.player.model.PlayerModel;
import org.springframework.stereotype.Component;

/**
 * @author Catherine
 * @create 2020-08-06 18:44
 */
@Component
public class PlayerManager implements InitManager {
    @Override
    public PlayerExample init(PlayerModel playerModel) {
        PlayerExample playerExample = new PlayerExample();
        PlayerExample.Criteria criteria = playerExample.createCriteria();
        criteria.andRoleidEqualTo(playerModel.getRoleid());
        return playerExample;
    }

    public Player newPlayer(String roleName, Integer roleType, Long userId) {
        Player player = new Player();
        player.setName(roleName);
        player.setOccupation(roleType);
        player.setId(userId);
        player.setLoc(1);
        player.setExp(1);
        player.setMoney(0);
        player.setEquip("{}");
        return player;
    }


}
