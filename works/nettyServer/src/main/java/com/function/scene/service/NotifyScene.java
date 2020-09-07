package com.function.scene.service;

import com.function.player.model.Player;
import com.function.scene.model.Scene;
import com.function.scene.model.SceneObject;
import com.function.scene.model.SceneObjectType;
import com.function.team.model.Team;
import com.function.user.map.PlayerMap;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Catherine
 */
@Service
public class NotifyScene {
    @Autowired
    private PlayerMap playerMap;

    public void notifyScene(Scene scene, String notify) {
        Map<Long, SceneObject> players = scene.getSceneObjectMap().get(SceneObjectType.PLAYER.getType());
        players.keySet().forEach(playerId -> {
            SceneObject sceneObject = players.get(playerId);
            if (sceneObject.getType() == SceneObjectType.PLAYER) {
                Player p = (Player) sceneObject;
                ChannelHandlerContext ctx = playerMap.getCtxPlayer(p.getTPlayer().getRoleId());
                ctx.writeAndFlush(notify);
            }

        });

    }

    public void notifyTeam(Team team, String notify) {
        team.getMembers().keySet().forEach(playerId -> {
            ChannelHandlerContext ctx = playerMap.getCtxPlayer(playerId);
            ctx.writeAndFlush(notify);
        });
    }

    public void notifyPlayer(Player player, String notify) {
        player.getChannelHandlerContext().writeAndFlush(notify);

    }

    public void notifyPlayer(Player player, StringBuilder notify) {
        player.getChannelHandlerContext().writeAndFlush(notify);

    }

}
