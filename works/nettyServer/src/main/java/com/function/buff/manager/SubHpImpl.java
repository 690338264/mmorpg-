package com.function.buff.manager;

import com.function.buff.service.PlayerKilledByTypes;
import com.function.monster.model.Monster;
import com.function.monster.service.MonsterService;
import com.function.player.model.Player;
import com.function.player.model.SceneObjectTask;
import com.function.player.service.PlayerService;
import com.function.scene.model.Scene;
import com.function.scene.model.SceneObject;
import com.function.scene.model.SceneObjectType;
import com.manager.ThreadPoolManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledFuture;

/**
 * 扣血器
 *
 * @author Catherine
 * @create 2020-09-15 18:52
 */
@Component
public class SubHpImpl {
    @Autowired
    private PlayerService playerService;
    @Autowired
    private MonsterService monsterService;
    @Autowired
    private PlayerKilledByTypes playerKilledByTypes;

    public void subHp(SceneObject attacker, SceneObject target, int hurt) {
        Scene scene = attacker.getNowScene();
        try {
            target.getLock().lock();
            if (target.getHp() <= 0) {
                return;
            }
            if (target.getHp() - hurt <= 0) {
                target.setHp(0);
                target.onDie();
                if (target.getType() == SceneObjectType.MONSTER) {
                    Monster monster = (Monster) target;
                    if (attacker.getType() == SceneObjectType.SUMMON) {
                        Player player = (Player) scene.getSceneObjectMap().get(SceneObjectType.PLAYER).get(attacker.getId());
                        playerService.killMonster(monster, scene, player);
                        return;
                    }
                    playerService.killMonster(monster, scene, (Player) attacker);
                    return;
                }
                if (target.getType() == SceneObjectType.PLAYER) {
                    playerKilledByTypes.killPlayer(attacker, target);
                    return;
                }
            }
            if (target.getHp() - hurt > target.getOriHp()) {
                target.setHp(target.getOriHp());
                return;
            }
            if (attacker.getType() == SceneObjectType.MONSTER) {
                Monster monster = (Monster) attacker;
                if (!monster.getHurtList().containsKey(target.getId())) {
                    return;
                }
            }
            target.setHp(target.getHp() - hurt);
            addHate(attacker, target, hurt);
        } finally {
            target.getLock().unlock();
        }
    }

    public void addHate(SceneObject attacker, SceneObject target, int hurt) {
        if (target.getType() == SceneObjectType.MONSTER) {
            Monster monster = (Monster) target;
            if (monster.getHurtList().isEmpty()) {
                monster.getHurtList().put(attacker.getId(), hurt);
                ScheduledFuture<?> task = ThreadPoolManager.loopThread(() -> {
                    if (monster.getHurtList().isEmpty()) {
                        monster.getTaskMap().get(SceneObjectTask.ATTACK).cancel(true);
                        monster.getTaskMap().remove(SceneObjectTask.ATTACK);
                    }
                    try {
                        Long hate = monsterService.hurtSort(monster);
                        monsterService.monsterAtk(monster, hate);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, 0, MonsterService.HURT_BEAT, monster.getExcelId());
                monster.getTaskMap().put(SceneObjectTask.ATTACK, task);
                return;
            }
            int oriHurt = monster.getHurtList().getOrDefault(attacker.getId(), 0);
            monster.getHurtList().put(attacker.getId(), oriHurt + hurt);
        }
    }
}