package com.function.monster.service;

import com.function.monster.model.Monster;
import com.function.skill.model.Skill;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class MonsterService {

    public boolean isMonsterDeath(Monster monster) {
        if (monster.getMonsterExcel().getHp() <= 0) {
            return true;
        } else {
            return false;
        }
    }

    public int monsterAtk(Monster monster) {
        Integer[] keys = monster.getMonsterExcel().getMonsterSkill().keySet().toArray(new Integer[0]);
        Skill skill = new Skill();
        while (true) {
            Random random = new Random();
            Integer randomKey = keys[random.nextInt(keys.length)];
            skill = monster.getMonsterExcel().getMonsterSkill().get(randomKey);
            if (skill.getStatus() == 1) {
                break;
            }
        }
        skill.setStatus(0);
        int hurt = monster.getMonsterExcel().getAggr() * skill.getBuff();
        return hurt;

    }
}
