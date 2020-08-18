package com.function.monster.service;

import com.function.monster.model.Monster;
import com.function.monster.timetask.CdTime;
import com.function.monster.timetask.ReviveTime;
import com.function.player.model.Player;
import com.function.scene.model.Scene;
import com.function.scene.service.NotifyScene;
import com.function.skill.model.Skill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.Timer;


/**
 * @author Catherine
 */
@Component
public class MonsterService {

    @Autowired
    private NotifyScene notifyScene;

    /**
     * 判断怪物是否死亡
     */
    public boolean isMonsterDeath(int index, Scene scene) {
        Monster monster = scene.getSceneExcel().getMonsters().get(index);
        if (monster.getSelfHp() <= 0) {
            scene.getSceneExcel().getMonsters().remove(index);
            Timer timer = monster.getTimer();
            ReviveTime t = new ReviveTime(monster, index, scene);
            timer.schedule(t, monster.getMonsterExcel().getReviveTime() * 1000);
            monster.setTarget(null);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 怪物进行攻击
     */
    public void monsterAtk(Monster monster, Player player) {

        Integer[] keys = monster.getCanUseSkill().keySet().toArray(new Integer[0]);
        Random random = new Random();
        Integer randomKey = keys[random.nextInt(keys.length)];
        Skill skill = monster.getCanUseSkill().get(randomKey);
        Timer skillTimer = skill.getTimer();
        monster.getCanUseSkill().remove(randomKey);
        int hurt = monster.getMonsterExcel().getAggr() * skill.getSkillExcel().getBuff();
        player.setHp(player.getHp() - hurt);
        CdTime cdTime = new CdTime(monster, randomKey, skill);
        skillTimer.schedule(cdTime, skill.getSkillExcel().getCd() * 1000);
        StringBuilder getHurt = new StringBuilder("[").append(monster.getMonsterExcel().getName())
                .append("]释放了技能[").append(skill).append("对您造成")
                .append(hurt).append("的伤害\n");
        notifyScene.notifyPlayer(player, getHurt);
    }
}
