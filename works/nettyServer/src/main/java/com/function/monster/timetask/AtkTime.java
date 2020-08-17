package com.function.monster.timetask;

import com.function.monster.model.Monster;
import com.function.player.model.Player;
import com.function.scene.model.Scene;

import java.util.TimerTask;

/**
 * @author Catherine
 * @create 2020-08-17 16:36
 */
public class AtkTime extends TimerTask {

    private Player player;
    private Scene scene;
    private Monster monster;

    public AtkTime(Player player, Monster monster, Scene scene) {
        this.player = player;
        this.monster = monster;
        this.scene = scene;
    }

    @Override
    public void run() {
        if (player.getHp() < 0 || monster.getSelfHp() < 0) {
            cancel();
        } else {
//            monsterService.monsterAtk(monster,player);
        }
    }

}
