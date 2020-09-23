package com.function.summon.service;

import com.function.player.model.Player;
import com.function.player.model.SceneObjectTask;
import com.function.player.service.PlayerService;
import com.function.scene.model.SceneObject;
import com.function.scene.model.SceneObjectType;
import com.function.summon.model.Summon;
import com.manager.ThreadPoolManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.RandomUtil;

import java.util.Map;
import java.util.concurrent.ScheduledFuture;

/**
 * @author Catherine
 * @create 2020-09-23 16:50
 */
@Component
public class SummonService {
    @Autowired
    private PlayerService playerService;
    private static final long JUMP = 4000;

    /**
     * 召唤兽攻击怪物
     */
    public void attackMonster(Player player, long target, SceneObjectType type) {
        Map<Long, SceneObject> summonMap = player.getNowScene().getSceneObjectMap().get(SceneObjectType.SUMMON);
        Long id = player.getTPlayer().getRoleId();
        Summon summon = (Summon) summonMap.get(id);
        if (summonMap.containsKey(id) && summon.getTaskMap().get(SceneObjectTask.ATTACK) == null) {
            ScheduledFuture<?> task = ThreadPoolManager.loopThread(() -> {
                Integer[] keys = summon.getById().getSummonSkill().keySet().toArray(new Integer[0]);
                int skillId = RandomUtil.ramInt(keys);
                playerService.useSkill(summon, skillId, target, type);
            }, 0, JUMP, id.intValue());
            summon.getTaskMap().put(SceneObjectTask.ATTACK, task);
        }
    }
}
