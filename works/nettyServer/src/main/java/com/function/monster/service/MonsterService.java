package com.function.monster.service;

import com.function.buff.service.BuffService;
import com.function.monster.model.Monster;
import com.function.player.model.Player;
import com.function.player.service.PlayerService;
import com.function.scene.model.Scene;
import com.function.scene.model.SceneObjectType;
import com.function.scene.service.NotifyScene;
import com.function.skill.model.Skill;
import com.function.user.map.UserMap;
import com.manager.ThreadPoolManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;


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
    @Autowired
    private UserMap userMap;


    /**
     * 怪物死亡
     */
    public void monsterDeath(Long index, Scene scene) {
        int type = SceneObjectType.MONSTER.getType();
        Monster monster = (Monster) scene.getSceneObjectMap().get(type).get(index);
        monster.setDeathTime(System.currentTimeMillis());
        buffService.removeBuff(monster);
        scene.getSceneObjectMap().get(type).remove(index);
        scene.getWaitForRevive().put(monster.getSceneId(), monster);
        monster.getHurtList().clear();
    }

    /**
     * 怪物复活
     */
    public void monsterRevive(Scene scene) {
        scene.getWaitForRevive().forEach((k, v) -> {
            Monster monster = scene.getWaitForRevive().get(k);
            if (System.currentTimeMillis() - monster.getDeathTime() > monster.getMonsterExcel().getReviveTime()) {
                scene.getWaitForRevive().remove(k);
                monster.setHp(monster.getOriHp());
                scene.getSceneObjectMap().get(SceneObjectType.MONSTER.getType()).put(monster.getSceneId(), monster);
            }
        });

    }

    /**
     * 怪物进行攻击
     */
    public void monsterAtk(Monster monster, Long playerId) {
        Player player = userMap.getPlayers(playerId);
        Integer[] keys = monster.getCanUseSkill().keySet().toArray(new Integer[0]);
        Random random = new Random();
        Integer randomKey = keys[random.nextInt(keys.length)];
        Skill skill = monster.getCanUseSkill().get(randomKey);
        monster.getCanUseSkill().remove(randomKey);
        int hurt = monster.getMonsterExcel().getAggr() * skill.getSkillExcel().getAtk();
        player.setHp(player.getHp() - hurt);
        ThreadPoolManager.runThread(() -> monster.getCanUseSkill().put(randomKey, skill), skill.getSkillExcel().getCd(), monster.getId());
        if (!playerService.playerDie(player, monster)) {
            player.getChannelHandlerContext().writeAndFlush("您受到了：" + hurt + "点的伤害    剩余血量为" + player.getHp() + '\n');
            buffService.buff(monster.getSceneId().intValue(), skill, player, monster, player.getNowScene());
        }
    }

    /**
     * 仇恨排名
     */
    public Long hurtSort(Monster monster) {
        Map<Long, Integer> hurtMap = monster.getHurtList();
        List<Map.Entry<Long, Integer>> list = new LinkedList<>(hurtMap.entrySet());
        Collections.sort(list, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        Map<Long, Integer> result = new LinkedHashMap<>();
        for (Map.Entry<Long, Integer> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result.entrySet().iterator().next().getKey();
    }
}
