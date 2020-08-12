package com.function.player.manager;

import com.jpa.entity.TPlayer;
import org.springframework.stereotype.Component;

/**
 * @author Catherine
 * @create 2020-08-06 18:44
 */
@Component
public class PlayerManager implements InitManager {

    public TPlayer newPlayer(String roleName, Integer roleType, Long userId) {
        TPlayer player = new TPlayer();
        player.setName(roleName);
        player.setOccupation(roleType);
        player.setUserId(userId);
        player.setLoc(1);
        player.setExp(1);
        player.setMoney(0);
        player.setEquip("{}");
        return player;
    }


}
