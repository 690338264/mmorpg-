package com.function.monster.service;

import com.function.monster.controller.Time;
import com.function.monster.model.Monster;
import com.function.skill.model.Skill;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.Timer;


/**
 * @author Catherine
 */
@Component
public class MonsterService {
    /**
     * 判断怪物是否死亡
     */
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

    /**
     * 怪物进行攻击
     */
    public int[] monsterAtk(Monster monster) {
        Integer[] keys = monster.getMonsterExcel().getMonsterSkill().keySet().toArray(new Integer[0]);
        Skill skill = new Skill();
        do {
            Random random = new Random();
            Integer randomKey = keys[random.nextInt(keys.length)];
            skill = monster.getMonsterExcel().getMonsterSkill().get(randomKey);
        } while (skill.getLastTime() != null && System.currentTimeMillis() - skill.getLastTime() < skill.getSkillExcel().getCd());

        skill.setLastTime(System.currentTimeMillis());
        int hurt = monster.getMonsterExcel().getAggr() * skill.getSkillExcel().getBuff();
        int[] a = new int[2];
        a[0] = hurt;
        a[1] = skill.getSkillId();
        return a;
    }
}
