package com.function.monster.timetask;

import com.function.monster.model.Monster;
import com.function.skill.model.Skill;

import java.util.TimerTask;

/**
 * @author Catherine
 * @create 2020-08-17 15:02
 */
public class CdTime extends TimerTask {
    private Monster monster;
    private int index;
    private Skill skill;

    public CdTime(Monster monster, int index, Skill skill) {
        this.monster = monster;
        this.index = index;
        this.skill = skill;
    }

    @Override
    public void run() {
        monster.getCanUseSkill().put(index, skill);
    }
}
