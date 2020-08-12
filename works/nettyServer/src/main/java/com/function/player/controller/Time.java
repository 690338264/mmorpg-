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

    @Override
    public void run() {
        if (player.getMp() < player.getOriMp()) {
            player.setMp(player.getMp() + 5);
        } else {
            cancel();
        }
    }
}
