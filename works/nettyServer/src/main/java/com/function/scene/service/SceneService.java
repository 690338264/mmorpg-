package com.function.scene.service;

import com.function.monster.model.Monster;
import com.function.npc.excel.NpcResource;
import com.function.player.model.Player;
import com.function.player.service.PlayerData;
import com.function.scene.excel.SceneExcel;
import com.function.scene.excel.SceneResource;
import com.function.scene.model.Scene;
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

    /**
     * 相邻场景
     */
    public void getNeighbor(Player player, ChannelHandlerContext ctx) {
        int locId = player.getLoc();
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
        Scene scene = new Scene();
        scene.setSceneId(sceneId);
        player.setNowScene(scene);
        player.setLoc(sceneId);
        playerData.updateLoc(player);
        scene.getSceneExcel().getPlayers().put(player.getRoleId(), player);
        StringBuilder welcome = new StringBuilder("欢迎玩家").append(player.getName()).append("来到场景\n");
        notifyScene.notifyScene(scene, welcome);
        return SceneResource.getSceneById(sceneId);
    }

    /**
     * 查看周围
     */
    public void aoi(Player player, ChannelHandlerContext ctx) {
        Scene scene = player.getNowScene();
        String[] npcs = scene.getSceneExcel().getNpc().split(",");
        ctx.writeAndFlush("您所在场景有NPC:\n");
        for (String s : npcs) {
            int npc = Integer.parseInt(s);
            ctx.writeAndFlush(NpcResource.getNpcById(npc).getName() + "--id为" + npc + "\n[Npc状态]");
            if (NpcResource.getNpcById(npc).getStatus() == 1) {
                ctx.writeAndFlush("存活！\n");
            } else {
                ctx.writeAndFlush("已死亡\n");
            }
        }
        ctx.writeAndFlush("您所在场景有怪物：\n");

        for (Integer key : scene.getSceneExcel().getMonsters().keySet()) {
            Monster monster = scene.getSceneExcel().getMonsters().get(key);
            if (monster.getSelfHp() <= 0) {
                ctx.writeAndFlush("id:[" + key + "]" + monster.getMonsterExcel().getName() + "  [已死亡]" + '\n');
            } else {
                ctx.writeAndFlush("id:[" + key + "]" + monster.getMonsterExcel().getName() + "  Hp[" + monster.getSelfHp() + "]" + '\n');
            }
        }
        ctx.writeAndFlush("可选择怪物进行攻击\n");
    }

}
