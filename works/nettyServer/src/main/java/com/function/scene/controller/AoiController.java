package com.function.scene.controller;

import com.Cmd;
import com.function.player.model.PlayerModel;
import com.function.scene.model.Scene;
import com.function.scene.service.SceneService;
import com.function.user.controller.UserController;
import com.handler.ControllerManager;
import io.netty.channel.ChannelHandlerContext;
import util.Msg;

import javax.annotation.Resource;
import java.util.List;

public class AoiController {

    {
        ControllerManager.add(Cmd.AOI,this::aoi);
    }
    @Resource
    UserController userController;

    @Resource
    SceneService sceneService;
    private void aoi(ChannelHandlerContext ctx, Msg msg){
        PlayerModel playerModel = userController.getPlayermodel();
        Scene scene = sceneService.getSceneByPlayer(playerModel);
        if (scene.getNpcMap().isEmpty()){
            ctx.writeAndFlush("没有人哦\n");
        }
        else {
            ctx.writeAndFlush("场景有NPC:");
            scene.getNpcMap().values().forEach(npc -> ctx.writeAndFlush(npc.display()+'\n'));
        }

    }
}
