package com.function.scene.controller;

import com.Cmd;
import com.function.player.model.Player;
import com.function.scene.service.InstanceService;
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
    private InstanceService instanceService;

    {
        ControllerManager.add(Cmd.LIST_INSTANCE, this::listInstance);
        ControllerManager.add(Cmd.PERSONAL_INSTANCE, this::personalInstance);
        ControllerManager.add(Cmd.TEAM_INSTANCE, this::teamInstance);
        ControllerManager.add(Cmd.INTO_INSTANCE, this::intoInstance);
    }

    private void listInstance(Player player, Msg msg) {
        instanceService.listInstance(player);
    }

    private void personalInstance(Player player, Msg msg) {
        String[] params = ParamNumCheck.numCheck(player, msg, 2);
        int instanceId = Integer.parseInt(params[1]);
        instanceService.personalCreate(player, instanceId);
    }

    private void teamInstance(Player player, Msg msg) {
        String[] params = ParamNumCheck.numCheck(player, msg, 2);
        int instanceId = Integer.parseInt(params[1]);
        instanceService.teamCreate(player, instanceId);
    }

    private void intoInstance(Player player, Msg msg) {
        instanceService.intoInstance(player);
    }
}
