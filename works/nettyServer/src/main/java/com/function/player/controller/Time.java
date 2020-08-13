package com.function.player.controller;

import com.function.player.model.Player;
import lombok.Data;

import java.util.TimerTask;

/**
 * @author Catherine
 * @create 2020-08-03 17:47
 */
@Data
public class Time extends TimerTask {
    private Player player;
    private static int i = 0;

    @Override
    public void run() {
        i++;
        if (player.getMp() < player.getOriMp()) {
            player.setMp(player.getMp() + 2);
            System.out.println(player.getMp());
            System.out.println(i);
        } else {
            player.getTimerMap().remove("mp");
            System.out.println(player.getTimerMap());
            cancel();
        }
    }
}
