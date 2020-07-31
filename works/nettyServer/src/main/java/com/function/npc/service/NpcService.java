package com.function.npc.service;

import com.function.npc.model.NpcResource;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Service;

@Service
public class NpcService {
    public void talkToNpc(ChannelHandlerContext ctx, int npcId) {
        ctx.writeAndFlush(NpcResource.getNpcById(npcId).getName() + "对你说：" + NpcResource.getNpcById(npcId).getText() + '\n');
    }
}
