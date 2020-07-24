package com.function.scene.controller;

import com.Cmd;
import com.function.player.model.PlayerModel;
import com.function.scene.model.Scene;
import com.function.scene.service.SceneService;
import com.function.user.controller.UserController;
import com.handler.ControllerManager;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import util.Msg;
import util.ParamNumCheck;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Component
public class SceneController {
    {
        ControllerManager.add(Cmd.MOVE_TO,this::moveto);
    }

    @Resource
    private SceneService sceneService;

    private void moveto(ChannelHandlerContext ctx, Msg msg){
        UserController userController = new UserController();
        PlayerModel playerModel = userController.getPlayermodel();
        String[] params = ParamNumCheck.numCheck(ctx,msg,2);
        int togo = Integer.valueOf(params[1]);
        List<Scene> sceneList = sceneService.getNeighbor(playerModel);
        int flag = -1;
        ctx.writeAndFlush("您当前位置可以前往：/n");
        for(int i = 0;i<sceneList.size();i++){
            ctx.writeAndFlush("场景id："+sceneList.get(i).getId()+"场景名称："+sceneList.get(i).getName()+'\n');
            if(togo == sceneList.get(i).getId()){
                flag = i;
            }
        }
        if(flag != -1){
            sceneService.moveTo(playerModel,togo);
            ctx.writeAndFlush(sceneList.get(flag).getName()+"欢迎您！");
        }
        else {
            ctx.writeAndFlush("您暂时还不可以到这里哦！");
        }
//        if(sceneService.canMove(playerModel,togo)){
//            sceneService.move(playerModel,togo);
//            ctx.writeAndFlush("您已到达："+scene.display()+'\n');
//        }else{
//            ctx.writeAndFlush("您暂时还不磕到达该处！！\n");
//        }
    }
}
