package com.function.scene.service;

import com.function.monster.model.Monster;
import com.function.npc.excel.NpcExcel;
import com.function.player.model.Player;
import com.function.player.service.PlayerData;
import com.function.scene.excel.SceneExcel;
import com.function.scene.excel.SceneResource;
import com.function.scene.manager.SceneMap;
import com.function.scene.model.Scene;
import com.jpa.entity.TPlayer;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        oldScene.getPlayerMap().remove(player.getTPlayer().getRoleId());
        Scene scene = sceneMap.getSceneCache().get(sceneId);
        player.getTPlayer().setLoc(sceneId);
        scene.getPlayerMap().put(player.getTPlayer().getRoleId(), player);
        player.setNowScene(scene);
        playerData.updateLoc(player);
        StringBuilder welcome = new StringBuilder("欢迎玩家").append(player.getTPlayer().getName()).append("来到场景\n");
        notifyScene.notifyScene(scene, welcome);
        return SceneResource.getSceneById(sceneId);
    }

    /**
     * 查看周围
     */
    public void aoi(Player player, ChannelHandlerContext ctx) {
        Scene scene = player.getNowScene();
        StringBuilder sb = new StringBuilder("您所在场景有NPC:\n");
        for (Integer key : scene.getNpcMap().keySet()) {
            NpcExcel npc = scene.getNpcMap().get(key);
            sb.append(npc.getName()).append("--id为").append(npc.getId()).append('\n');
        }
        sb.append("您所在的场景有玩家：").append('\n');
        for (Long key : scene.getPlayerMap().keySet()) {
            TPlayer tPlayer = scene.getPlayerMap().get(key).getTPlayer();
            sb.append("[").append(tPlayer.getName()).append("]")
                    .append("等级为：[").append(tPlayer.getLevel()).append("]\n");
        }
        sb.append("您所在场景有怪物：").append('\n');
        for (Integer key : scene.getMonsterMap().keySet()) {
            Monster monster = scene.getMonsterMap().get(key);
            if (monster.getSelfHp() <= 0) {
                sb.append("id:[").append(key).append("]")
                        .append(monster.getMonsterExcel().getName()).append("  [已死亡]").append('\n');
            } else {
                sb.append("id:[").append(key).append("]")
                        .append(monster.getMonsterExcel().getName()).append("  Hp[")
                        .append(monster.getSelfHp()).append("]").append('\n');
            }
        }
        sb.append("可选择怪物进行攻击\n");
        notifyScene.notifyPlayer(player, sb);
    }

}
