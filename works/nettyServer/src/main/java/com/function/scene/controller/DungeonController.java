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
public class DungeonController {
    @Autowired
    private DungeonService dungeonService;

    {
        ControllerManager.add(Cmd.LIST_DUNGEON, this::listDungeon);
        ControllerManager.add(Cmd.PERSONAL_DUNGEON, this::personalDungeon);
        ControllerManager.add(Cmd.TEAM_DUNGEON, this::teamDungeon);
        ControllerManager.add(Cmd.INTO_DUNGEON, this::intoDungeon);
    }

    private void listDungeon(Player player, Msg msg) {
        dungeonService.listDungeon(player);
    }

    private void personalDungeon(Player player, Msg msg) {
        String[] params = ParamNumCheck.numCheck(player, msg, 2);
        int dungeonId = Integer.parseInt(params[1]);
        dungeonService.personalCreate(player, dungeonId);
    }

    private void teamDungeon(Player player, Msg msg) {
        String[] params = ParamNumCheck.numCheck(player, msg, 2);
        int dungeonId = Integer.parseInt(params[1]);
        dungeonService.teamCreate(player, dungeonId);
    }

    private void intoDungeon(Player player, Msg msg) {
        dungeonService.intoDungeon(player);
    }
}
