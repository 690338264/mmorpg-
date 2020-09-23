package com.function.buff.service;

import com.event.model.playerEvent.PvpWinEvent;
import com.function.player.model.Player;
import com.function.player.model.SceneObjectTask;
import com.function.scene.model.SceneObject;
import com.function.scene.model.SceneObjectType;
import com.function.scene.service.NotifyScene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Catherine
 * @create 2020-09-23 20:01
 */
@Component
public class PlayerKilledByTypes {
    @Autowired
    private NotifyScene notifyScene;

    private final Map<SceneObjectType, PlayerDie> playerDieMap = new HashMap<>();

    {
        playerDieMap.put(SceneObjectType.MONSTER, this::monsterKill);
        playerDieMap.put(SceneObjectType.SUMMON, this::summonKill);
        playerDieMap.put(SceneObjectType.PLAYER, this::playerKill);
    }

    public void killPlayer(SceneObject attacker, SceneObject target) {
        playerDieMap.get(attacker.getType()).playerDie(attacker, target);
    }

    public void monsterKill(SceneObject attacker, SceneObject target) {
        notifyScene.notifyPlayer(target, MessageFormat.format("{0}阵亡\n", target.getName()));
    }

    public void summonKill(SceneObject attacker, SceneObject target) {
        attacker.getTaskMap().get(SceneObjectTask.ATTACK).cancel(true);
        attacker.getTaskMap().remove(SceneObjectTask.ATTACK);
        Player player = (Player) attacker.getNowScene().getSceneObjectMap().get(SceneObjectType.PLAYER).get(attacker.getId());
        player.asynchronousSubmitEvent(new PvpWinEvent());
        notifyScene.notifyScene(attacker.getNowScene(),
                MessageFormat.format("{0}击败了{1}\n", player.getName(), target.getName()));
    }

    public void playerKill(SceneObject attacker, SceneObject target) {
        Player player = (Player) attacker;
        player.asynchronousSubmitEvent(new PvpWinEvent());
        notifyScene.notifyScene(attacker.getNowScene(),
                MessageFormat.format("{0}击败了{1}\n", attacker.getName(), target.getName()));
    }
}
