package com.function.chat.controller;

import com.Cmd;
import com.function.chat.service.CommunicateService;
import com.function.player.model.Player;
import com.handler.ControllerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.Msg;
import util.ParamNumCheck;

/**
 * @author Catherine
 * @create 2020-08-20 17:30
 */
@Component
public class CommunicateController {

    @Autowired
    private CommunicateService communicateService;

    {
        ControllerManager.add(Cmd.WHISPER, this::whisper);
        ControllerManager.add(Cmd.SPEAK, this::talkToAll);
    }

    private void whisper(Player player, Msg msg) {
        String[] params = ParamNumCheck.numCheck(player, msg, 3);
        long toTalk = Long.parseLong(params[1]);
        String text = params[2];
        communicateService.whisper(player, toTalk, text);
    }

    private void talkToAll(Player player, Msg msg) {
        String[] params = ParamNumCheck.numCheck(player, msg, 2);
        String text = params[1];
        communicateService.speak(player, text);
    }
}
