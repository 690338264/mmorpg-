package com.function.buff.service;

import com.function.buff.model.Buff;
import com.function.monster.model.Monster;
import com.function.player.model.Player;
import com.function.player.service.PlayerService;
import com.function.scene.model.Scene;
import com.function.scene.model.SceneObject;
import com.function.scene.model.SceneObjectType;
import com.function.scene.service.NotifyScene;
import com.function.skill.model.Skill;
import com.manager.ThreadPoolManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Random;
import java.util.concurrent.ScheduledFuture;

/**
 * @author Catherine
 * @create 2020-08-27 18:01
 */
@Component
public class BuffService {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private NotifyScene notifyScene;

    /**
     * 移除Buff
     */
    public void removeBuff(SceneObject sceneObject) {
        sceneObject.getBuffs().forEach((k, v) -> {
            sceneObject.getBuffs().get(k).cancel(true);
            sceneObject.getBuffs().remove(k);
        });
    }

    /**
     * buff效果
     */
    public void buff(int id, Skill skill, SceneObject beAttack, SceneObject attacker, Scene scene) {
        skill.getBuffMap().forEach((k, v) -> {
            SceneObject buffer;
            //正面  负面状态
            Buff buff = skill.getBuffMap().get(k);
            Random random = new Random();
            double a = random.nextDouble();
            if (a > buff.getBuffExcel().getRate()) {
                return;
            }
            int flag;
            if (buff.getBuffExcel().getState() == 1) {
                flag = 1;
                buffer = attacker;
            } else {
                flag = -1;
                buffer = beAttack;
            }
            buff.setSceneObject(attacker);
            //如果已有该buff  覆盖
            ScheduledFuture scheduledFuture = buffer.getBuffs().get(buff.getId());
            if (scheduledFuture != null) {
                scheduledFuture.cancel(true);
                buffer.getBuffs().remove(buff.getId());
                buffer.setAtk(buffer.getAtk() - flag * buff.getBuffExcel().getAtk());
            }
            long time = buff.getBuffExcel().getLast() / buff.getBuffExcel().getTimes();
            buff.setRemainTimes(buff.getBuffExcel().getTimes());
            //多次效果
            if (buff.getBuffExcel().getTimes() != 1) {
                ScheduledFuture buffTask = ThreadPoolManager.loopThread(() -> {
                    if (buff.getRemainTimes() == 0) {
                        buffer.getBuffs().get(buff.getId()).cancel(true);
                    }
                    buffer.setHp(buffer.getHp() + flag * buff.getBuffExcel().getHp());
                    System.out.println("Hp" + flag * buff.getBuffExcel().getHp());
                    buff.setRemainTimes(buff.getRemainTimes() - 1);
                    if (buffer.getHp() <= 0) {
                        if (buffer.getType() == SceneObjectType.MONSTER.getType()) {
                            Monster m = (Monster) buffer;
                            playerService.killMonster(m, scene, m.getSceneId(), (Player) attacker);
                        } else {
                            Monster m = (Monster) attacker;
                            Player p = (Player) buffer;
                            playerService.playerDie(p, m);
                        }
                    }
                    if (buffer.getHp() > buffer.getOriHp()) {
                        buffer.setHp(buffer.getOriHp());
                    }
                }, time, time, id);
                buffer.getBuffs().put(buff.getId(), buffTask);
            }
            //单次持续效果
            else {
                int atk = buffer.getAtk();
                buffer.setAtk(atk + flag * buff.getBuffExcel().getAtk());
                ScheduledFuture buffTask = ThreadPoolManager.runThread(() -> {
                    buffer.setAtk(atk);
                }, time, id);
                buffer.getBuffs().put(buff.getId(), buffTask);

            }
            String target = buffer.getType() == SceneObjectType.MONSTER.getType() ? "怪物:" : "玩家";
            notifyScene.notifyScene(scene, MessageFormat.format("{0}{1}获得buff:{2}  [{3}]\n",
                    target, buffer.getName(), buff.getBuffExcel().getName(), buff.getBuffExcel().getDescribe()));
        });
    }

}
