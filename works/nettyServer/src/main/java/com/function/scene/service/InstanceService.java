package com.function.scene.service;

import com.function.player.model.Player;
import com.function.scene.excel.SceneExcel;
import com.function.scene.excel.SceneResource;
import com.function.scene.manager.SceneManager;
import com.function.scene.model.Instance;
import com.function.scene.model.Scene;
import com.function.scene.model.SceneType;
import com.function.team.manager.TeamManager;
import com.function.team.model.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Map;

/**
 * @author Catherine
 * @create 2020-08-28 18:23
 */
@Component
public class InstanceService {
    @Autowired
    private NotifyScene notifyScene;
    @Autowired
    private SceneManager sceneManager;
    @Autowired
    private TeamManager teamManager;
    @Autowired
    private SceneService sceneService;

    /**
     * 列出可以创建的副本
     */
    public void listInstance(Player player) {
        Map<Integer, SceneExcel> map = SceneResource.getSceneMap(SceneType.PRIVATE.getType());
        notifyScene.notifyPlayer(player, "个人副本:\n");
        map.forEach((k, v) -> {
            SceneExcel sceneExcel = map.get(k);
            notifyScene.notifyPlayer(player, MessageFormat.format("{0}:{1}\n", sceneExcel.getId(), sceneExcel.getName()));
        });
        Map<Integer, SceneExcel> map1 = SceneResource.getSceneMap(SceneType.TEAM.getType());
        notifyScene.notifyPlayer(player, "组队副本:\n");
        map1.forEach((k, v) -> {
            SceneExcel sceneExcel = map1.get(k);
            notifyScene.notifyPlayer(player, MessageFormat.format("{0},{1}\n", sceneExcel.getId(), sceneExcel.getName()));
        });
    }

    /**
     * 个人副本创建
     */
    public void personalCreate(Player player, int id) {
        if (player.getTeamId() != null || player.getInstance() != null) {
            notifyScene.notifyPlayer(player, "副本创建失败!\n");
            return;
        }
        int type = SceneType.PRIVATE.getType();
        Instance instance = createInstance(type, id);
        instance.getPlayers().add(player);
        player.setInstance(instance);
        notifyScene.notifyPlayer(player, "副本创建成功，请尽快进入副本!\n");
    }

    /**
     * 小队副本创建
     */
    public void teamCreate(Player player, int id) {
        Team team = teamManager.getTeamCache().get(player.getTeamId());
        if (player.getTeamId() == null || !team.getLeaderId().equals(player.getTPlayer().getRoleId()) || player.getInstance() != null) {
            notifyScene.notifyPlayer(player, "创建失败!\n");
            return;
        }
        int type = SceneType.TEAM.getType();
        Instance instance = createInstance(type, id);
        team.getMembers().forEach((k, v) -> {
            Player teammate = team.getMembers().get(k);
            instance.getPlayers().add(teammate);
            teammate.setInstance(instance);
            notifyScene.notifyPlayer(teammate, "副本创建成功,请尽快进入副本\n");
        });
    }

    /**
     * 副本场景创建
     */
    public Instance createInstance(int type, int id) {
        Scene scene = sceneManager.createScene(type, id);
        Instance instance = new Instance(scene);
        nextBoss(instance);
        instance.setCreateTime(System.currentTimeMillis());
        sceneManager.instanceStart(scene.getId(), instance);
        return instance;
    }

    /**
     * 按顺序初始化怪物
     */
    public boolean nextBoss(Instance instance) {
        if (!sceneManager.createMonster(instance.getScene(), instance.getNextBoss())) {
            return false;
        } else {
            instance.setNextBoss(instance.getNextBoss() + 1);
            return true;
        }

    }

    /**
     * 进入副本
     */
    public void intoInstance(Player player) {
        Scene scene = player.getInstance().getScene();
        sceneService.addPlayer(scene.getType(), scene.getId(), player);
        notifyScene.notifyScene(scene, MessageFormat.format("[{0}]进入副本\n", player.getTPlayer().getName()));
    }

    /**
     * 副本销毁
     */
    public void destroy(Instance instance) {
        instance.getPlayers().forEach(player -> {
            player.setInstance(null);
            sceneService.addPlayer(SceneType.PUBLIC.getType(), player.getTPlayer().getLoc(), player);
            notifyScene.notifyPlayer(player, "您已退出副本!\n");
        });
        instance.getPlayers().clear();
        instance.getScene().getHeartBeat().cancel(true);
    }
}
