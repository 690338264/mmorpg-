package com.function.communicate.service;

import com.function.player.model.Player;
import com.function.scene.manager.SceneMap;
import com.function.scene.service.NotifyScene;
import com.function.user.map.PlayerMap;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Catherine
 * @create 2020-08-20 17:32
 */
@Component
public class CommunicateService {
    @Autowired
    private PlayerMap playerMap;
    @Autowired
    private SceneMap sceneMap;
    @Autowired
    private NotifyScene notifyScene;

    public void whisper(Player player, long playerId, String text) {
        ChannelHandlerContext ctxPlayer = playerMap.getCtxPlayer(playerId);
        StringBuilder result = new StringBuilder();
        if (ctxPlayer == null) {
            result.append("玩家不在线或不存在！\n");
            notifyScene.notifyPlayer(player, result);
            return;
        }
        Player toTalk = playerMap.getPlayerCtx(ctxPlayer);
        result.append(player.getTPlayer().getName()).append("对你说：").append(text).append('\n');
        notifyScene.notifyPlayer(toTalk, result);
    }

    public void speak(Player player, String text) {
        StringBuilder sentence = new StringBuilder(player.getTPlayer().getName())
                .append("说：").append(text).append('\n');
        for (int key : sceneMap.getSceneCache().keySet()) {
            notifyScene.notifyScene(sceneMap.getSceneCache().get(key), sentence);
        }

    }
}
