package com.function.scene.controller;

import com.Cmd;
import com.function.player.model.Player;
import com.function.scene.service.DungeonService;
import com.handler.ControllerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.Msg;
import util.ParamNumCheck;

/**
 * @author Catherine
 * @create 2020-08-28 17:00
 */
@Component
public class InstanceController {
    @Autowired
    private DungeonService dungeonService;

    {
        ControllerManager.add(Cmd.LIST_INSTANCE, this::listInstance);
        ControllerManager.add(Cmd.PERSONAL_INSTANCE, this::personalInstance);
        ControllerManager.add(Cmd.TEAM_INSTANCE, this::teamInstance);
        ControllerManager.add(Cmd.INTO_INSTANCE, this::intoInstance);
    }

    private void listInstance(Player player, Msg msg) {
        dungeonService.listInstance(player);
    }

    private void personalInstance(Player player, Msg msg) {
        String[] params = ParamNumCheck.numCheck(player, msg, 2);
        int instanceId = Integer.parseInt(params[1]);
        dungeonService.personalCreate(player, instanceId);
    }

    private void teamInstance(Player player, Msg msg) {
        String[] params = ParamNumCheck.numCheck(player, msg, 2);
        int instanceId = Integer.parseInt(params[1]);
        dungeonService.teamCreate(player, instanceId);
    }

    private void intoInstance(Player player, Msg msg) {
        dungeonService.intoInstance(player);
    }
}
