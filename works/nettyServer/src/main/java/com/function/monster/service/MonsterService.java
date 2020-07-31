package com.function.monster.service;

import com.function.monster.model.Monster;
import com.function.skill.excel.SkillExcel;
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
        SkillExcel skillExcel = new SkillExcel();
//        while (true) {
        Random random = new Random();
        Integer randomKey = keys[random.nextInt(keys.length)];
        skillExcel = monster.getMonsterExcel().getMonsterSkill().get(randomKey);
//            if (skillExcel.getStatus() == 1) {
//                break;
//            }
//        }
//        skillExcel.setStatus(0);
        int hurt = monster.getMonsterExcel().getAggr() * skillExcel.getBuff();
        return hurt;

    }
}
