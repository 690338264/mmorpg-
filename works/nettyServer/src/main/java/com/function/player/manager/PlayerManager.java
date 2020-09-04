package com.function.player.manager;

import com.function.user.map.PlayerMap;
import com.function.user.map.UserMap;
import com.jpa.entity.TPlayer;
import com.manager.ThreadPoolManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author Catherine
 * @create 2020-08-06 18:44
 */
@Component
public class PlayerManager {
    @Autowired
    private PlayerMap playerMap;
    @Autowired
    private UserMap userMap;

    private static final long outlineTime = 3600 * 60 * 24;

    private static final long jump = 5000;

    public TPlayer newPlayer(String roleName, Integer roleType, Long userId) {
        TPlayer player = new TPlayer();
        player.setName(roleName);
        player.setOccupation(roleType);
        player.setUserId(userId);
        player.setLoc(1000000);
        player.setExp(1);
        player.setMoney(0);
        player.setEquip("{}");
        return player;
    }

    @PostConstruct
    private void check() {
        ThreadPoolManager.loopThread(() -> {
            playerMap.getOfflinePlayer().forEach((playerId, time) -> {
                if (System.currentTimeMillis() - time > outlineTime) {
                    userMap.getPlayers().remove(playerId);
                    playerMap.getOfflinePlayer().remove(playerId);
                }
            });
        }, 0, jump, getClass().hashCode());
    }
}
