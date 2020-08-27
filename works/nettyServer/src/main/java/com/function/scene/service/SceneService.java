package com.function.scene.service;

import com.function.monster.model.Monster;
import com.function.npc.excel.NpcExcel;
import com.function.player.model.Player;
import com.function.player.service.PlayerData;
import com.function.scene.excel.SceneExcel;
import com.function.scene.excel.SceneResource;
import com.function.scene.manager.SceneMap;
import com.function.scene.model.Scene;
import com.function.scene.model.SceneObject;
import com.function.scene.model.SceneObjectType;
import com.function.user.service.UserService;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

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
    private SceneMap sceneMap;


    /**
     * 相邻场景
     */
    public void getNeighbor(Player player, ChannelHandlerContext ctx) {
        int locId = player.getTPlayer().getLoc();
        String neighbors = SceneResource.getSceneById(locId).getNeighbor();
        String[] strs = neighbors.split(",");
        ctx.writeAndFlush("您现在所在场景为：" + SceneResource.getSceneById(locId).getName() + "\n您可到达的地点有：");
        for (String str : strs) {
            int canTo = Integer.parseInt(str);
            ctx.writeAndFlush(SceneResource.getSceneById(canTo).getName() + "代号为：" + SceneResource.getSceneById(canTo).getId() + '\n');
        }
    }

    /**
     * 移动场景
     */
    public SceneExcel moveTo(Player player, int sceneId) {
        Scene oldScene = sceneMap.getSceneCache().get(player.getTPlayer().getLoc());
        oldScene.getSceneObjectMap().remove(player.getTPlayer().getRoleId());
        Scene scene = sceneMap.getSceneCache().get(sceneId);
        player.getTPlayer().setLoc(sceneId);
        scene.getSceneObjectMap().put(UserService.Player + player.getTPlayer().getRoleId(), player);
        player.setNowScene(scene);
        playerData.updateLoc(player);
        notifyScene.notifyScene(scene, MessageFormat.format("欢迎玩家{0}来到场景\n", player.getTPlayer().getName()));
        return SceneResource.getSceneById(sceneId);
    }

    /**
     * 查看周围
     */
    public void aoi(Player player, ChannelHandlerContext ctx) {
        Scene scene = player.getNowScene();
        for (String key : scene.getSceneObjectMap().keySet()) {
            SceneObject sceneObject = scene.getSceneObjectMap().get(key);
            if (sceneObject.getType() == SceneObjectType.NPC.getType()) {
                NpcExcel npc = (NpcExcel) sceneObject;
                notifyScene.notifyPlayer(player, MessageFormat.format("NPC:{0}  id为{1}\n",
                        npc.getName(), npc.getId()));
                continue;
            }
            if (sceneObject.getType() == SceneObjectType.PLAYER.getType()) {
                Player p = (Player) sceneObject;
                String die = p.getHp() <= 0 ? "[阵亡]" : "";
                notifyScene.notifyPlayer(player, MessageFormat.format("玩家:[{0}]等级为{1}  {2}\n",
                        p.getTPlayer().getName(), p.getTPlayer().getLevel(), die));
                continue;
            }
            if (sceneObject.getType() == SceneObjectType.MONSTER.getType()) {
                Monster monster = (Monster) sceneObject;
                notifyScene.notifyPlayer(player, MessageFormat.format("怪物:id:[{0}]{1}  Hp:[{2}]\n",
                        monster.getSceneId(), monster.getMonsterExcel().getName(), monster.getHp()));
            }
        }

    }

}
