package com.function.npc.controller;

import com.Cmd;
import com.function.npc.service.NpcService;
import com.function.player.model.Player;
import com.function.user.service.UserService;
import com.handler.ControllerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.Msg;
import util.ParamNumCheck;

/**
 * @author Catherine
 */
@Component
public class NpcController {
    {
        ControllerManager.add(Cmd.TALK_TO, this::speak);
    }

    @Autowired
    private NpcService npcService;
    @Autowired
    private UserService userService;

    private void speak(Player player, Msg msg) {
        String[] params = ParamNumCheck.numCheck(player, msg, 2);
        Long npcId = Long.parseLong(params[1]);
        npcService.talkToNpc(player, npcId);
    }
}
