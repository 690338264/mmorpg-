package com.function.monster.controller;

import com.function.monster.model.Monster;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.TimerTask;

/**
 * @author Catherine
 * @create 2020-08-03 15:23
 */
@Data
@Component
public class Time extends TimerTask {

    private Monster monster;

    @Override
    public void run() {
        monster.setSelfHp(monster.getMonsterExcel().getHp());
    }
}
