package com.function.player.manager;

import com.function.player.model.PlayerInfo;
import com.function.user.map.PlayerMap;
import com.function.user.map.UserMap;
import com.jpa.dao.PlayerInfoDAO;
import com.jpa.entity.TPlayer;
import com.manager.ThreadPoolManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
    @Autowired
    private PlayerInfoDAO playerInfoDAO;

    private final Map<Long, PlayerInfo> playerInfoMap = new ConcurrentHashMap<>();

    private static final long OUTLINE_TIME = 3600 * 60 * 24;

    private static final long JUMP = 5000;

    public Map<Long, PlayerInfo> getPlayerInfoMap() {
        return playerInfoMap;
    }

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
        ThreadPoolManager.loopThread(() -> playerMap.getOfflinePlayer().forEach((playerId, time) -> {
            if (System.currentTimeMillis() - time > OUTLINE_TIME) {
                userMap.getPlayers().remove(playerId);
                playerMap.getOfflinePlayer().remove(playerId);
            }
        }), 0, JUMP, getClass().hashCode());
    }

    @PostConstruct
    private void init() {
        playerInfoDAO.findAll().forEach((tPlayerInfo) -> {
            PlayerInfo playerInfo = new PlayerInfo(tPlayerInfo);
            playerInfoMap.put(tPlayerInfo.getPlayerId(), playerInfo);
        });
    }
}
