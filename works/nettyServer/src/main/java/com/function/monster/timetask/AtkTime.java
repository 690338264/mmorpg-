package com.function.monster.timetask;

import com.function.monster.model.Monster;
import com.function.player.model.Player;
import com.function.scene.model.Scene;
import com.function.skill.model.Skill;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Catherine
 * @create 2020-08-17 16:36
 */

public class AtkTime extends TimerTask {

    private Player player;
    private Scene scene;
    private Monster monster;

    public AtkTime(Player player, Monster monster, Scene scene) {
        this.player = player;
        this.monster = monster;
        this.scene = scene;
    }


    @Override
    public void run() {
        if (player.getHp() <= 0 || monster.getSelfHp() <= 0) {
            cancel();
        } else {
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
            player.getChannelHandlerContext().writeAndFlush("您受到了：" + hurt + "点的伤害    剩余血量为" + player.getHp() + '\n');
        }
    }

}
