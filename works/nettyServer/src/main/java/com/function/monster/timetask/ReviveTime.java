package com.function.monster.timetask;

import com.function.monster.model.Monster;
import com.function.scene.model.Scene;

import java.util.TimerTask;

/**
 * @author Catherine
 * @create 2020-08-03 15:23
 */
public class ReviveTime extends TimerTask {

    private Monster monster;
    private int index;
    private Scene scene;

    public ReviveTime(Monster monster, int index, Scene scene) {
        this.monster = monster;
        this.index = index;
        this.scene = scene;
    }

    @Override
    public void run() {
        scene.getMonsterMap().put(index, monster);
        monster.setSelfHp(monster.getMonsterExcel().getHp());
    }
}
