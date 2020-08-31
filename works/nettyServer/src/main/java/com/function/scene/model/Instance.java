package com.function.scene.model;

import com.function.player.model.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Catherine
 * @create 2020-08-31 10:19
 */
public class Instance {
    private Scene scene;
    /**
     * 可以进入的玩家
     */
    private List<Player> players = new ArrayList<>();

    private int nextBoss;

    private Long createTime;

    public Instance(Scene scene) {
        this.scene = scene;
    }

    public Scene getScene() {
        return scene;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public int getNextBoss() {
        return nextBoss;
    }

    public void setNextBoss(int nextBoss) {
        this.nextBoss = nextBoss;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
