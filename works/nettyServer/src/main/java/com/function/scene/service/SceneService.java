package com.function.scene.service;

import com.function.monster.model.MonsterResource;
import com.function.npc.model.NpcResource;
import com.function.player.model.PlayerModel;
import com.function.player.service.PlayerService;
import com.function.scene.model.Scene;
import com.function.scene.model.SceneResource;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SceneService {

    @Autowired
    private PlayerService playerService;

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

    public Scene moveTo(PlayerModel playerModel, int sceneId) {
        playerModel.setNowScene(SceneResource.getSceneById(sceneId));
        //playerModel.setLoc(sceneId);
        playerService.updateLoc(sceneId, playerModel);
        return SceneResource.getSceneById(sceneId);
    }

    public void aoi(PlayerModel playerModel, ChannelHandlerContext ctx) {
        Scene scene = playerModel.getNowScene();
        String[] npcs = scene.getNpc().split(",");
        String[] monsters = scene.getMonster().split(",");
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
        for (int i = 0; i < monsters.length; i++) {
            int monster = Integer.parseInt(monsters[i]);
            ctx.writeAndFlush(MonsterResource.getMonById(monster).getName() + " [Hp]" + MonsterResource.getMonById(monster).getHp() + '\n');
        }
    }

}
