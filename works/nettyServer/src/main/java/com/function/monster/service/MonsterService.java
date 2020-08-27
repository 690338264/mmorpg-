package com.function.monster.service;

import com.function.buff.service.BuffService;
import com.function.monster.model.Monster;
import com.function.player.model.Player;
import com.function.player.service.PlayerService;
import com.function.scene.excel.SceneResource;
import com.function.scene.model.Scene;
import com.function.scene.service.NotifyScene;
import com.function.skill.model.Skill;
import com.manager.ThreadPoolManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;


/**
 * @author Catherine
 */
@Component
public class MonsterService {

    @Autowired
    private NotifyScene notifyScene;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private BuffService buffService;


    /**
     * 怪物死亡
     */
    public void monsterDeath(String index, Scene scene) {
        Monster monster = (Monster) scene.getSceneObjectMap().get(index);
        buffService.removeBuff(monster);
        scene.getSceneObjectMap().remove(index);
        String id = index.replaceAll(SceneResource.Monster, "");
        ThreadPoolManager.runThread(() -> {
            monster.setHp(monster.getMonsterExcel().getHp());
            scene.getSceneObjectMap().put(index, monster);
        }, monster.getMonsterExcel().getReviveTime(), Integer.parseInt(id));
        monster.setTarget(null);
    }

    /**
     * 怪物进行攻击
     */
    public void monsterAtk(Monster monster, Player player) {

        Integer[] keys = monster.getCanUseSkill().keySet().toArray(new Integer[0]);
        Random random = new Random();
        Integer randomKey = keys[random.nextInt(keys.length)];
        Skill skill = monster.getCanUseSkill().get(randomKey);
        monster.getCanUseSkill().remove(randomKey);
        int hurt = monster.getMonsterExcel().getAggr() * skill.getSkillExcel().getAtk();
        player.setHp(player.getHp() - hurt);
        ThreadPoolManager.runThread(() -> {
            monster.getCanUseSkill().put(randomKey, skill);
        }, skill.getSkillExcel().getCd(), monster.getId());
        if (!playerService.playerDie(player, monster)) {
            player.getChannelHandlerContext().writeAndFlush("您受到了：" + hurt + "点的伤害    剩余血量为" + player.getHp() + '\n');
            playerService.buff(monster.getSceneId(), skill, player, monster, player.getNowScene());
        }
    }
}
