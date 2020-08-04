package com.function.monster.service;

import com.function.monster.controller.Time;
import com.function.monster.model.Monster;
import com.function.skill.model.Skill;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.Timer;


@Component
public class MonsterService {

    public boolean isMonsterDeath(Monster monster) {
        if (monster.getSelfHp() <= 0) {
            Timer timer = new Timer();
            Time t = new Time();
            t.setMonster(monster);
            timer.schedule(t, monster.getMonsterExcel().getReviveTime() * 1000);
            return true;
        } else {
            return false;
        }
    }

    public int[] monsterAtk(Monster monster) {
        Integer[] keys = monster.getMonsterExcel().getMonsterSkill().keySet().toArray(new Integer[0]);
        Skill skill = new Skill();
        while (true) {
            Random random = new Random();
            Integer randomKey = keys[random.nextInt(keys.length)];
            skill = monster.getMonsterExcel().getMonsterSkill().get(randomKey);
            skill.setNowTime(System.currentTimeMillis());
            if (skill.getLastTime() == null || skill.getNowTime() - skill.getLastTime() >= skill.getSkillExcel().getCd()) {
                break;
            }
        }
        skill.setLastTime(System.currentTimeMillis());
        int hurt = monster.getMonsterExcel().getAggr() * skill.getSkillExcel().getBuff();
        int[] a = new int[2];
        a[0] = hurt;
        a[1] = skill.getSkillId();
        return a;
    }
}
