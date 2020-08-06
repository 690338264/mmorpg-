package com.function.player.controller;

import com.function.player.model.PlayerModel;
import lombok.Data;

import java.util.TimerTask;

/**
 * @author Catherine
 * @create 2020-08-03 17:47
 */
@Data
public class Time extends TimerTask {
    private PlayerModel playerModel;

    @Override
    public void run() {
        if (playerModel.getMp() < playerModel.getOriMp()) {
            playerModel.setMp(playerModel.getMp() + 5);
        } else {
            cancel();
        }
    }
}
