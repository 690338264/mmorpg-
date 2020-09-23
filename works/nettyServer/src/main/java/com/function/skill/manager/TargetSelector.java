package com.function.skill.manager;

import com.function.buff.excel.BuffExcel;
import com.function.buff.excel.BuffResource;
import com.function.buff.model.BuffType;
import com.function.item.model.Item;
import com.function.player.model.Player;
import com.function.scene.model.Scene;
import com.function.scene.model.SceneObject;
import com.function.scene.model.SceneObjectType;
import com.function.scene.service.NotifyScene;
import com.function.skill.model.Skill;
import com.function.team.manager.TeamManager;
import com.function.team.model.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Catherine
 * @create 2020-09-14 21:20
 */
@Component
public class TargetSelector {
    @Autowired
    private TeamManager teamManager;
    @Autowired
    private NotifyScene notifyScene;

    /**
     * 技能是否可以释放
     */
    public boolean checkIfCan(SceneObject attacker, int skillId) {
        Skill skill = attacker.getCanUseSkill().get(skillId);
        //判断是否有技能
        if (skill == null) {
            notifyScene.notifyPlayer(attacker, "技能不存在\n");
            return false;
        }
        //判断技能CD
        if (System.currentTimeMillis() - skill.getLastTime() < skill.getSkillExcel().getCd()) {
            notifyScene.notifyPlayer(attacker, "CD还没好\n");
            return false;
        }
        //判断mp
        if (attacker.getMp() < skill.getSkillExcel().getMp()) {
            notifyScene.notifyPlayer(attacker, "mp不够\n");
            return false;
        }
        //判断装备磨损度
        if (attacker.getClass() == Player.class) {
            Player player = (Player) attacker;
            for (Item equipment : player.getEquipMap().values()) {
                if (equipment.getNowWear() <= 10) {
                    notifyScene.notifyPlayer(attacker, "请先修理装备\n");
                    return false;
                }
                equipment.setNowWear(equipment.getNowWear() - 2);
            }
        }
        return true;
    }

    /**
     * 选择buff作用目标
     */
    public Map<Integer, List<SceneObject>> chooseTarget(SceneObject attacker, int skillId, long targetId, SceneObjectType type) {
        Map<Integer, List<SceneObject>> targets = new HashMap<>();
        Scene scene = attacker.getNowScene();
        List<Integer> buffIds = attacker.getCanUseSkill().get(skillId).getSkillExcel().getBuffId();
        buffIds.forEach(buffId -> {
            targets.computeIfAbsent(buffId, key -> new ArrayList<>());
            BuffExcel buffExcel = BuffResource.getBuffById(buffId);
            if (buffExcel.getTargetType() == BuffType.FRIEND.getType()) {
                if (attacker.getClass() == Player.class) {
                    addTeamMate(attacker, targets.get(buffId));
                    return;
                }
                targets.get(buffId).add(attacker);
                return;
            }
            if (attacker.getId() == targetId && attacker.getType() == type) {
                return;
            }
            if (!ifLegal(scene, targetId, type)) {
                return;
            }
            int targetNum = type == SceneObjectType.PLAYER ? 1 : buffExcel.getTargetNumber();
            addTargets(targetId, scene, type, targetNum, targets.get(buffId));
        });
        return targets;
    }

    /**
     * 添加队友
     */
    public void addTeamMate(SceneObject sceneObject, List<SceneObject> targets) {
        Player player = (Player) sceneObject;
        if (player.getTeamId() == null) {
            targets.add(player);
            return;
        }
        Team team = teamManager.getTeamCache().get(player.getTeamId());
        team.getMembers().forEach((memberId, member) -> targets.add(member));
    }

    /**
     * 对象是否合法
     */
    public boolean ifLegal(Scene scene, long targetId, SceneObjectType type) {
        return scene.getSceneObjectMap().get(type).containsKey(targetId);
    }

    /**
     * 添加目标
     */
    public void addTargets(Long targetId, Scene scene, SceneObjectType type, int targetNum, List<SceneObject> targets) {
        SceneObject target = scene.getSceneObjectMap().get(type).get(targetId);
        //转为list
        List<SceneObject> objectList = new ArrayList<>(scene.getSceneObjectMap().get(type).values());
        int remainNum = Math.min(targetNum, objectList.size());
        int index = objectList.indexOf(target);
        for (int targetIndex = index; targetIndex < objectList.size() && remainNum != 0; targetIndex++, remainNum--) {
            targets.add(objectList.get(targetIndex));
        }
        if (remainNum == 0) {
            return;
        }
        for (int targetIndex = index - remainNum; remainNum != 0; targetIndex++, remainNum--) {
            targets.add(objectList.get(targetIndex));
        }
    }


}
