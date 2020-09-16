package com.function.buff.service;

import com.function.buff.excel.BuffExcel;
import com.function.buff.manager.SubHpImpl;
import com.function.buff.model.Buff;
import com.function.buff.model.EffectType;
import com.function.scene.model.SceneObject;
import com.function.scene.model.SceneObjectState;
import com.function.scene.service.NotifyScene;
import com.manager.ThreadPoolManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.ScriptEngineUtil;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

/**
 * @author Catherine
 * @create 2020-09-15 19:05
 */
@Component
public class BuffEffectsRealize {
    @Autowired
    private SubHpImpl subHp;
    @Autowired
    private NotifyScene notifyScene;

    private final Map<Integer, BuffEffect> buffEffectMap = new HashMap<>();

    {
        buffEffectMap.put(EffectType.DAMAGE.getType(), this::damage);
        buffEffectMap.put(EffectType.HP_BUFF.getType(), this::buffForHp);
        buffEffectMap.put(EffectType.ATK_BUFF.getType(), this::buffForAtk);
        buffEffectMap.put(EffectType.VERTIGO.getType(), this::vertigoBuff);
    }

    public void effect(SceneObject attacker, List<SceneObject> targets, BuffExcel buffExcel) {
        buffEffectMap.get(buffExcel.getType()).buffEffect(attacker, targets, buffExcel);
    }

    /**
     * 普通伤害
     */
    private void damage(SceneObject attacker, List<SceneObject> targets, BuffExcel buffExcel) {
        int hurt = (int) ScriptEngineUtil.eval(buffExcel.getHp(), attacker.getAttributeMap());
        targets.forEach(target -> {
            if (target.getState() == SceneObjectState.DEATH) {
                return;
            }
            notifyScene.notifyScene(attacker.getNowScene(), MessageFormat.format("{0}对{1}进行攻击,造成了{2}点伤害\n",
                    attacker.getName(), target.getName(), hurt));
            subHp.subHp(attacker, target, hurt);

        });
    }

    /**
     * hp改变的buff
     */
    private void buffForHp(SceneObject attacker, List<SceneObject> targets, BuffExcel buffExcel) {
        int hpChange = (int) ScriptEngineUtil.eval(buffExcel.getHp(), attacker.getAttributeMap());
        targets.forEach(target -> {
            //目标死亡不执行
            if (target.getState() == SceneObjectState.DEATH) {
                return;
            }
            Buff buff = new Buff(buffExcel.getId());
            long time = buffExcel.getLast() / buffExcel.getTimes();
            buff.setRemainTimes(buff.getBuffExcel().getTimes());
            notifyScene.notifyScene(attacker.getNowScene(), MessageFormat.format("{0}受到buff{1}:{2}\n",
                    target.getName(), buffExcel.getName(), buffExcel.getDescribe()));
            checkBuffAndRemove(target, buff);
            ScheduledFuture<?> buffTask = ThreadPoolManager.loopThread(() -> {
                //效果结束或者玩家不在场景内
                if (buff.getRemainTimes() == 0 || attacker.getNowScene() != target.getNowScene()) {
                    target.getBuffs().get(buff.getId()).cancel(true);
                    target.getBuffs().remove(buff.getId());
                }
                //扣血
                notifyScene.notifyScene(target.getNowScene(), MessageFormat.format("{0}hp{1}\n",
                        target.getName(), hpChange > 0 ? "-" + hpChange : -hpChange));
                subHp.subHp(attacker, target, hpChange);
                buff.setRemainTimes(buff.getRemainTimes() - 1);
            }, time, time, target.getId().intValue());
            target.getBuffs().put(buff.getId(), buffTask);
        });
    }

    /**
     * 攻击力改变的buff
     */
    private void buffForAtk(SceneObject attacker, List<SceneObject> targets, BuffExcel buffExcel) {
        int atkChange = (int) ScriptEngineUtil.eval(buffExcel.getAtk(), attacker.getAttributeMap());
        Buff buff = new Buff(buffExcel.getId());
        buff.setAtkChange(atkChange);
        long time = buffExcel.getLast() / buffExcel.getTimes();
        targets.forEach(target -> {
            if (target.getState() == SceneObjectState.DEATH) {
                return;
            }
            checkBuffAndRemove(target, buff);
            notifyScene.notifyScene(attacker.getNowScene(), MessageFormat.format("{0}受到buff{1}:{2}\n",
                    target.getName(), buffExcel.getName(), buffExcel.getDescribe()));
            target.setAtk(target.getAtk() + atkChange);
            ScheduledFuture<?> buffTask = ThreadPoolManager.delayThread(() -> {
                target.setAtk(target.getAtk() - atkChange);
                target.getBuffs().remove(buff.getId());
            }, time, target.getId().intValue());
            target.getBuffs().put(buff.getId(), buffTask);
        });
    }

    private void vertigoBuff(SceneObject attacker, List<SceneObject> targets, BuffExcel buffExcel) {
        targets.forEach(target -> {
            if (target.getState() == SceneObjectState.DEATH) {
                return;
            }
            Buff buff = new Buff(buffExcel.getId());
            checkBuffAndRemove(target, buff);
            target.setState(SceneObjectState.DIZZY);
            notifyScene.notifyScene(attacker.getNowScene(), MessageFormat.format("{0}受到buff{1}:{2}\n",
                    target.getName(), buffExcel.getName(), buffExcel.getDescribe()));
            ScheduledFuture<?> buffTask = ThreadPoolManager.delayThread(() -> {
                if (target.getState() == SceneObjectState.DEATH) {
                    return;
                }
                target.setState(SceneObjectState.NORMAL);
            }, buffExcel.getLast(), target.getId().intValue());
            target.getBuffs().put(buff.getId(), buffTask);
        });
    }

    /**
     * 检查并移除buff
     */
    private void checkBuffAndRemove(SceneObject target, Buff buff) {
        ScheduledFuture<?> scheduledFuture = target.getBuffs().get(buff.getId());
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
            target.getBuffs().remove(buff.getId());
            target.setAtk(target.getAtk() - buff.getAtkChange());
        }
    }
}
