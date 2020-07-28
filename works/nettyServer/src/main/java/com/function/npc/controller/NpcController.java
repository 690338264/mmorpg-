package com.function.npc.controller;

import com.Cmd;
import com.function.npc.service.NpcService;
import com.handler.ControllerManager;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.Msg;
import util.ParamNumCheck;

@Component
public class NpcController {
    {
        ControllerManager.add(Cmd.TALK_TO, this::speak);
    }

    @Autowired
    private NpcService npcService;

    private void speak(ChannelHandlerContext ctx, Msg msg) {
        String[] params = ParamNumCheck.numCheck(ctx, msg, 2);
        int npcId = Integer.valueOf(params[1]);
        npcService.talkToNpc(ctx,npcId);
    }
}
