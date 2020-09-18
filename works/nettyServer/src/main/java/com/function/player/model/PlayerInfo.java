package com.function.player.model;

import com.jpa.entity.TPlayerInfo;

/**
 * @author Catherine
 * @create 2020-09-06 02:33
 */
public class PlayerInfo {
    private TPlayerInfo tPlayerInfo;

    public PlayerInfo(TPlayerInfo tPlayerInfo) {
        this.tPlayerInfo = tPlayerInfo;
    }

    public TPlayerInfo gettPlayerInfo() {
        return tPlayerInfo;
    }

    public void settPlayerInfo(TPlayerInfo tPlayerInfo) {
        this.tPlayerInfo = tPlayerInfo;
    }
}
