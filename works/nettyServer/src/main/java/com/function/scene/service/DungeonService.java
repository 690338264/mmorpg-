package com.function.scene.service;

import com.function.player.model.Player;
import com.function.scene.excel.SceneExcel;
import com.function.scene.excel.SceneResource;
import com.function.scene.manager.SceneManager;
import com.function.scene.model.Dungeon;
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
public class DungeonService {
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
    public void listDungeon(Player player) {
        Map<Integer, SceneExcel> map = SceneResource.getSceneMap(SceneType.PRIVATE.getType());
        notifyScene.notifyPlayer(player, "个人副本:\n");
        map.forEach((sceneId, sceneExcel) ->
                notifyScene.notifyPlayer(player, MessageFormat.format("{0}:{1}\n", sceneId, sceneExcel.getName()))
        );
        Map<Integer, SceneExcel> map1 = SceneResource.getSceneMap(SceneType.TEAM.getType());
        notifyScene.notifyPlayer(player, "组队副本:\n");
        map1.forEach((sceneId, sceneExcel) ->
                notifyScene.notifyPlayer(player, MessageFormat.format("{0},{1}\n", sceneId, sceneExcel.getName())));
    }

    /**
     * 个人副本创建
     */
    public void personalCreate(Player player, int id) {
        if (player.getTeamId() != null || player.getDungeon() != null) {
            notifyScene.notifyPlayer(player, "副本创建失败!\n");
            return;
        }
        int type = SceneType.PRIVATE.getType();
        Dungeon dungeon = createDungeon(type, id, player.getTPlayer().getRoleId().intValue());
        dungeon.getPlayers().add(player);
        player.setDungeon(dungeon);
        notifyScene.notifyPlayer(player, "副本创建成功，请尽快进入副本!\n");
    }

    /**
     * 小队副本创建
     */
    public void teamCreate(Player player, int dungeonId) {
        Team team = teamManager.getTeamCache().get(player.getTeamId());
        if (player.getTeamId() == null || !team.getLeaderId().equals(player.getTPlayer().getRoleId()) || player.getDungeon() != null) {
            notifyScene.notifyPlayer(player, "创建失败!\n");
            return;
        }
        int type = SceneType.TEAM.getType();
        Dungeon dungeon = createDungeon(type, dungeonId, player.getTeamId().intValue());
        team.getMembers().forEach((k, teammate) -> {
            dungeon.getPlayers().add(teammate);
            teammate.setDungeon(dungeon);
            notifyScene.notifyPlayer(teammate, "副本创建成功,请尽快进入副本\n");
        });
    }

    /**
     * 副本场景创建
     */
    public Dungeon createDungeon(int type, int sceneId, int id) {
        Scene scene = sceneManager.createScene(type, sceneId, id);
        Dungeon dungeon = new Dungeon(scene);
        nextBoss(dungeon);
        dungeon.setCreateTime(System.currentTimeMillis());
        sceneManager.dungeonStart(scene.getId(), dungeon);
        return dungeon;
    }

    /**
     * 按顺序初始化怪物
     */
    public boolean nextBoss(Dungeon dungeon) {
        if (!sceneManager.createMonster(dungeon.getScene(), dungeon.getNextBoss())) {
            return false;
        } else {
            dungeon.setNextBoss(dungeon.getNextBoss() + 1);
            return true;
        }

    }

    /**
     * 进入副本
     */
    public void intoDungeon(Player player) {
        Scene scene = player.getDungeon().getScene();
        sceneService.addPlayer(scene.getType(), scene.getId(), player);
        notifyScene.notifyScene(scene, MessageFormat.format("[{0}]进入副本\n", player.getTPlayer().getName()));
    }

    /**
     * 副本销毁
     */
    public void destroy(Dungeon dungeon) {
        dungeon.getPlayers().forEach(player -> {
            player.setDungeon(null);
            sceneService.addPlayer(SceneType.PUBLIC.getType(), player.getTPlayer().getLoc(), player);
            notifyScene.notifyPlayer(player, "您已退出副本!\n");
        });
        dungeon.getPlayers().clear();
        dungeon.getScene().getHeartBeat().cancel(true);
        sceneManager.get(dungeon.getScene().getType()).remove(dungeon.getScene().getId());
    }
}
