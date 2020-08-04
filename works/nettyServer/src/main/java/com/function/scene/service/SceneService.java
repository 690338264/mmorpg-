package com.function.scene.service;

import com.function.monster.model.Monster;
import com.function.npc.excel.NpcResource;
import com.function.player.model.PlayerModel;
import com.function.player.service.PlayerService;
import com.function.scene.excel.SceneExcel;
import com.function.scene.excel.SceneResource;
import com.manager.NotifyScene;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SceneService {

    @Autowired
    private PlayerService playerService;
    @Autowired
    private NotifyScene notifyScene;

    public void getNeighbor(PlayerModel playerModel, ChannelHandlerContext ctx) {
        int locId = playerModel.getLoc();
        String neighbors = SceneResource.getSceneById(locId).getNeighbor();
        String[] strs = neighbors.split(",");
        ctx.writeAndFlush("您现在所在场景为：" + SceneResource.getSceneById(locId).getName() + "\n您可到达的地点有：");
        for (int i = 0; i < strs.length; i++) {
            int canTo = Integer.parseInt(strs[i]);
            ctx.writeAndFlush(SceneResource.getSceneById(canTo).getName() + "代号为：" + SceneResource.getSceneById(canTo).getId() + '\n');
        }
    }

    public SceneExcel moveTo(PlayerModel playerModel, int sceneId) {
        SceneExcel sceneExcel = SceneResource.getSceneById(sceneId);
        playerModel.setNowScene(sceneExcel);
        playerModel.setLoc(sceneId);
        playerService.updateLoc(sceneId, playerModel);
        sceneExcel.getPlayers().put(playerModel.getRoleid(), playerModel);
        notifyScene.notifyScene(sceneExcel, "欢迎玩家" + playerModel.getName() + "来到场景\n");
        return SceneResource.getSceneById(sceneId);
    }

    public void aoi(PlayerModel playerModel, ChannelHandlerContext ctx) {
        SceneExcel sceneExcel = playerModel.getNowScene();
        String[] npcs = sceneExcel.getNpc().split(",");
        ctx.writeAndFlush("您所在场景有NPC:\n");
        for (int i = 0; i < npcs.length; i++) {
            int npc = Integer.parseInt(npcs[i]);
            ctx.writeAndFlush(NpcResource.getNpcById(npc).getName() + "--id为" + npc + "\n[Npc状态]");
            if (NpcResource.getNpcById(npc).getStatus() == 1) {
                ctx.writeAndFlush("存活！\n");
            } else {
                ctx.writeAndFlush("已死亡\n");
            }
        }
        ctx.writeAndFlush("您所在场景有怪物：\n");

        for (Integer key : sceneExcel.getMonsters().keySet()) {
            Monster monster = sceneExcel.getMonsters().get(key);
            if (monster.getSelfHp() <= 0) {
                ctx.writeAndFlush("id:[" + key + "]" + monster.getMonsterExcel().getName() + "  [已死亡]" + '\n');
            } else {
                ctx.writeAndFlush("id:[" + key + "]" + monster.getMonsterExcel().getName() + "  Hp[" + monster.getSelfHp() + "]" + '\n');
            }
        }
        ctx.writeAndFlush("可选择怪物进行攻击\n");
    }

}
