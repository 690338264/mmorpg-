package com.function.monster.service;

import com.function.monster.model.Monster;
import com.function.player.model.Player;
import com.function.player.service.PlayerService;
import com.function.scene.model.Scene;
import com.function.scene.model.SceneObjectState;
import com.function.scene.model.SceneObjectType;
import com.function.user.map.UserMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.RandomUtil;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * @author Catherine
 */
@Component
public class MonsterService {

    @Autowired
    private PlayerService playerService;
    @Autowired
    private UserMap userMap;
    public static final long HURT_BEAT = 3000;

    /**
     * 怪物复活
     */
    public void monsterRevive(Scene scene) {
        scene.getWaitForRevive().forEach((monsterId, monster) -> {
            if (System.currentTimeMillis() - monster.getDeathTime() > monster.getMonsterExcel().getReviveTime()) {
                scene.getWaitForRevive().remove(monsterId);
                monster.setHp(monster.getOriHp());
                monster.setState(SceneObjectState.NORMAL);
                scene.getSceneObjectMap().get(SceneObjectType.MONSTER).put(monsterId, monster);
            }
        });

    }

    /**
     * 怪物进行攻击
     */
    public void monsterAtk(Monster monster, Long playerId) {
        if (monster.getState() == SceneObjectState.DIZZY) {
            return;
        }
        Integer[] keys = monster.getCanUseSkill().keySet().toArray(new Integer[0]);
        int skillId = RandomUtil.ramInt(keys);
        playerService.useSkill(monster, skillId, playerId, SceneObjectType.PLAYER);
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
        monster.getHurtList().forEach((playerId, hurt) -> {
            Player player = userMap.getPlayers(playerId);
            if (player.getNowScene() != monster.getNowScene()) {
                monster.getHurtList().remove(playerId);
            }
        });
    }
}
