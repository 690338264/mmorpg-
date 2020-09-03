package com.function.scene.service;

import com.function.monster.model.Monster;
import com.function.npc.excel.NpcExcel;
import com.function.player.model.Player;
import com.function.player.service.PlayerData;
import com.function.scene.excel.SceneExcel;
import com.function.scene.excel.SceneResource;
import com.function.scene.manager.SceneManager;
import com.function.scene.model.Scene;
import com.function.scene.model.SceneObject;
import com.function.scene.model.SceneObjectType;
import com.function.scene.model.SceneType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Map;

/**
 * @author Catherine
 */
@Service
public class SceneService {

    @Autowired
    private PlayerData playerData;
    @Autowired
    private NotifyScene notifyScene;
    @Autowired
    private SceneManager sceneManager;


    /**
     * 相邻场景
     */
    public void getNeighbor(Player player) {
        int locId = player.getTPlayer().getLoc();
        int type = SceneType.PUBLIC.getType();
        String neighbors = SceneResource.getSceneById(type, locId / SceneResource.idTimes).getNeighbor();
        String[] strs = neighbors.split(",");
        notifyScene.notifyPlayer(player, MessageFormat.format("您现在所在场景为：{0}\n您可到达的地点有：\n",
                sceneManager.get(type).get(locId).getSceneExcel().getName()));
        for (String str : strs) {
            int canTo = Integer.parseInt(str);
            SceneExcel sceneExcel = SceneResource.getSceneById(type, canTo);
            notifyScene.notifyPlayer(player, MessageFormat.format("{0}代号为:{1}\n",
                    sceneExcel.getName(), sceneExcel.getId()));
        }
    }

    /**
     * 移动场景
     */
    public void moveTo(Player player, int sceneId) {
        int type = SceneType.PUBLIC.getType();
        Scene oldScene = sceneManager.get(type).get(player.getTPlayer().getLoc());
        if (!oldScene.getSceneExcel().getNeighbor().contains(String.valueOf(sceneId))) {
            notifyScene.notifyPlayer(player, "您还不能到达该处!\n");
            return;
        }
        oldScene.getSceneObjectMap().get(SceneObjectType.PLAYER.getType()).remove(player.getTPlayer().getRoleId());
        for (int key : sceneManager.get(type).keySet()) {
            if (key / SceneResource.idTimes == sceneId) {
                sceneId = key;
            }
        }
        player.getTPlayer().setLoc(sceneId);
        Scene scene = addPlayer(type, sceneId, player);
        playerData.updatePlayerInfo(player);
        notifyScene.notifyScene(scene, MessageFormat.format("欢迎玩家{0}来到场景\n", player.getTPlayer().getName()));
        notifyScene.notifyPlayer(player, MessageFormat.format("您已到达{0}\n", scene.getSceneExcel().getName()));
    }

    /**
     * 查看周围
     */
    public void aoi(Player player) {
        Scene scene = player.getNowScene();
        for (SceneObjectType type : SceneObjectType.values()) {
            Map<Long, SceneObject> map = scene.getSceneObjectMap().get(type.getType());
            map.forEach((k, v) -> {
                SceneObject sceneObject = map.get(k);
                if (sceneObject.getType() == SceneObjectType.NPC.getType()) {
                    NpcExcel npc = (NpcExcel) sceneObject;
                    notifyScene.notifyPlayer(player, MessageFormat.format("NPC:{0}  id为{1}\n",
                            npc.getName(), npc.getId()));
                    return;
                }
                if (sceneObject.getType() == SceneObjectType.PLAYER.getType()) {
                    Player p = (Player) sceneObject;
                    String die = p.getHp() <= 0 ? "[阵亡]" : "";
                    notifyScene.notifyPlayer(player, MessageFormat.format("玩家:[{0}]等级为{1}  {2}\n",
                            p.getTPlayer().getName(), p.getTPlayer().getLevel(), die));
                    return;
                }
                if (sceneObject.getType() == SceneObjectType.MONSTER.getType()) {
                    Monster monster = (Monster) sceneObject;
                    notifyScene.notifyPlayer(player, MessageFormat.format("怪物:id:[{0}]{1}  Hp:[{2}]\n",
                            monster.getId(), monster.getMonsterExcel().getName(), monster.getHp()));
                }
            });
        }
    }

    public Scene addPlayer(int type, int sceneId, Player player) {
        Scene scene = sceneManager.get(type).get(sceneId);
        scene.getSceneObjectMap().get(SceneObjectType.PLAYER.getType()).put(player.getTPlayer().getRoleId(), player);
        player.setNowScene(scene);
        return scene;
    }
}
