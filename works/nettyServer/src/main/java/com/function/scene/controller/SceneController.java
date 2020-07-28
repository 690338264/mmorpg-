package com.function.scene.controller;

import com.Cmd;
import com.database.entity.Player;
import com.function.player.model.PlayerModel;
import com.function.scene.model.Scene;
import com.function.scene.service.SceneService;
import com.function.user.controller.UserController;
import com.function.user.service.UserService;
import com.handler.ControllerManager;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.Msg;
import util.ParamNumCheck;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Component
public class SceneController {
    {
        ControllerManager.add(Cmd.WHERE_CAN_GO, this::getNeighbor);
        ControllerManager.add(Cmd.MOVE_TO, this::moveTo);
        ControllerManager.add(Cmd.AOI, this::aoi);
    }

    @Autowired
    private SceneService sceneService;
    @Autowired
    private UserService userService;

    private void getNeighbor(ChannelHandlerContext ctx, Msg msg) {
        PlayerModel playerModel = userService.getPlayerByCtx(ctx);
        sceneService.getNeighbor(playerModel, ctx);
    }

    private void moveTo(ChannelHandlerContext ctx, Msg msg) {
        PlayerModel playerModel = userService.getPlayerByCtx(ctx);
        String[] params = ParamNumCheck.numCheck(ctx, msg, 2);
        int sceneId = Integer.valueOf(params[1]);
        Scene scene = sceneService.moveTo(playerModel, sceneId);
        ctx.writeAndFlush("您已到达：" + scene.getName() + '\n');
    }

    private void aoi(ChannelHandlerContext ctx, Msg msg) {
        PlayerModel playerModel = userService.getPlayerByCtx(ctx);
        sceneService.aoi(playerModel,ctx);
    }
}
