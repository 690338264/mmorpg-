package com.function.monster.service;

import com.function.buff.service.BuffService;
import com.function.monster.model.Monster;
import com.function.player.model.Player;
import com.function.player.model.SceneObjectTask;
import com.function.player.service.PlayerService;
import com.function.scene.model.Scene;
import com.function.scene.model.SceneObjectType;
import com.function.scene.service.NotifyScene;
import com.function.skill.model.Skill;
import com.function.user.map.UserMap;
import com.manager.ThreadPoolManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ScheduledFuture;


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
        scene.getWaitForRevive().put(monster.getId(), monster);
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
                scene.getSceneObjectMap().get(SceneObjectType.MONSTER.getType()).put(monster.getId(), monster);
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
        ScheduledFuture skillCd = ThreadPoolManager.delayThread(() ->
                monster.getCanUseSkill().put(randomKey, skill), skill.getSkillExcel().getCd(), monster.getExcelId());
        monster.getTaskMap().put(SceneObjectTask.SKILL_CD.getKey(), skillCd);
        notifyScene.notifyPlayer(player, MessageFormat.format("您受到了{0}点伤害", hurt));
        if (!playerService.playerDie(player, monster)) {
            notifyScene.notifyPlayer(player, MessageFormat.format("剩余血量为{0}\n", player.getHp()));
            buffService.buff(monster.getId().intValue(), skill, player, monster, player.getNowScene());
        }
    }

    /**
     * 仇恨排名
     */
    public Long hurtSort(Monster monster) {
        inScene(monster);
        Map<Long, Integer> hurtMap = monster.getHurtList();
        List<Map.Entry<Long, Integer>> list = new LinkedList<>(hurtMap.entrySet());
        list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        Map<Long, Integer> result = new LinkedHashMap<>();
        for (Map.Entry<Long, Integer> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result.entrySet().iterator().next().getKey();
    }

    /**
     * 移除仇恨列表中移出场景的玩家
     */
    public void inScene(Monster monster) {
        monster.getHurtList().forEach((k, v) -> {
            Player player = userMap.getPlayers(k);
            if (player.getNowScene().getSceneId() != monster.getSceneId()) {
                monster.getHurtList().remove(k);
            }
        });
    }
}
