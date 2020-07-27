package com.function.scene.service;

import com.function.player.model.PlayerModel;
import com.function.scene.model.Scene;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Service;
import util.excel.ImportExcel;
import util.excel.Judge;

import java.util.*;

@Service
public class SceneService {

    public void getNeighbor(PlayerModel playerModel, ChannelHandlerContext ctx) {
        int locId = playerModel.getLoc();
        ImportExcel sceneExcel = new ImportExcel();
        List<List<String>> list = sceneExcel.read("D:\\javaweb\\nettyjoin\\nettyServer\\src\\main\\resources\\excels\\scene.xlsx");
        String[] sid = list.get(locId).get(2).split(",");
        ctx.writeAndFlush("您想在所在场景为：" + list.get(locId).get(1) + '\n');
        for (int i = 0; i < sid.length; i++) {
            int canTo = Integer.parseInt(sid[i]);
            ctx.writeAndFlush("您可到达的地点有：" + list.get(canTo).get(1) + '\n');
        }
    }

    public void moveTo(PlayerModel playerModel,int sceneId){
        playerModel.setLoc(sceneId);
    }

}
