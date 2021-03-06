package com.function.chat.service;

import com.function.player.model.Player;
import com.function.scene.service.NotifyScene;
import com.function.user.map.PlayerMap;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

/**
 * @author Catherine
 * @create 2020-08-20 17:32
 */
@Component
public class CommunicateService {
    @Autowired
    private PlayerMap playerMap;
    @Autowired
    private NotifyScene notifyScene;

    /**
     * 玩家私聊
     */
    public void whisper(Player player, long playerId, String text) {
        ChannelHandlerContext ctxPlayer = playerMap.getCtxPlayer(playerId);
        if (ctxPlayer == null) {
            notifyScene.notifyPlayer(player, "玩家不在线或不存在！\n");
            return;
        }
        Player toTalk = playerMap.getPlayerCtx(ctxPlayer);

        notifyScene.notifyPlayer(toTalk, MessageFormat.format("{0}对你说：{1}\n", player.getTPlayer().getName(), text));
    }

    /**
     * 全服喊话
     */
    public void speak(Player player, String text) {
        playerMap.getPlayerCtxMap().forEach((ctx, players) ->
                notifyScene.notifyPlayer(players, MessageFormat.format("{0}说：{1}\n",
                        player.getTPlayer().getName(), text)));
    }
}
