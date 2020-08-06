package com.function.scene.service;

import com.function.monster.model.Monster;
import com.function.npc.excel.NpcResource;
import com.function.player.model.PlayerModel;
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
    public void getNeighbor(PlayerModel playerModel, ChannelHandlerContext ctx) {
        int locId = playerModel.getLoc();
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
    public SceneExcel moveTo(PlayerModel playerModel, int sceneId) {
        Scene scene = new Scene();
        scene.setSceneId(sceneId);
        playerModel.setNowScene(scene);
        playerModel.setLoc(sceneId);
        playerData.updateLoc(sceneId, playerModel);
        scene.getSceneExcel().getPlayers().put(playerModel.getRoleid(), playerModel);
        notifyScene.notifyScene(scene, "欢迎玩家" + playerModel.getName() + "来到场景\n");
        return SceneResource.getSceneById(sceneId);
    }

    /**
     * 查看周围
     */
    public void aoi(PlayerModel playerModel, ChannelHandlerContext ctx) {
        Scene scene = playerModel.getNowScene();
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
