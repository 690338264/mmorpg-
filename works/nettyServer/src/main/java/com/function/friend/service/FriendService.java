package com.function.friend.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.event.model.playerEvent.FriendAddEvent;
import com.function.player.manager.PlayerManager;
import com.function.player.model.Player;
import com.function.player.service.PlayerData;
import com.function.quest.model.Quest;
import com.function.quest.model.QuestState;
import com.function.quest.model.QuestType;
import com.function.scene.service.NotifyScene;
import com.function.user.map.PlayerMap;
import com.function.user.map.UserMap;
import com.jpa.dao.PlayerDAO;
import com.jpa.entity.TPlayer;
import com.jpa.entity.TPlayerInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * @author Catherine
 * @create 2020-09-09 17:36
 */
@Component
public class FriendService {
    @Autowired
    private NotifyScene notifyScene;
    @Autowired
    private PlayerMap playerMap;
    @Autowired
    private UserMap userMap;
    @Autowired
    private PlayerManager playerManager;
    @Autowired
    private PlayerData playerData;
    @Autowired
    private PlayerDAO playerDAO;

    /**
     * 查看好友列表
     */
    public void listFriends(Player player) {
        notifyScene.notifyPlayer(player, "好友列表\n");
        player.getFriend().forEach((id, tFriend) -> {
            String online = playerMap.getCtxPlayer(id) == null ? "离线" : "在线";
            notifyScene.notifyPlayer(player, MessageFormat.format("[{0}] {1} {2} [{3}]\n",
                    id, tFriend.getName(), tFriend.getOccupation(), online));
        });
        notifyScene.notifyPlayer(player, "好友请求\n");
        player.getFriendRequest().forEach((id, request) ->
                notifyScene.notifyPlayer(player, MessageFormat.format("[{0}] {1} {2}\n",
                        id, request.getName(), request.getOccupation())));
    }

    /**
     * 发送添加好友请求
     */
    public void sendApply(Player player, long applyId) {
        long playerId = player.getTPlayer().getRoleId();
        TPlayerInfo request = playerManager.getPlayerInfoMap().get(playerId).gettPlayerInfo();
        if (player.getFriend().containsKey(applyId)) {
            notifyScene.notifyPlayer(player, "已经是好友了！\n");
            return;
        }
        if (player.getFriendRequest().containsKey(applyId)) {
            notifyScene.notifyPlayer(player, "ta已请求添加你为好友\n");
            return;
        }
        if (userMap.getPlayers().containsKey(applyId)) {
            Player beApply = userMap.getPlayers().get(applyId);
            if (beApply.getFriendRequest().containsKey(playerId)) {
                notifyScene.notifyPlayer(player, "已添加过了哦!\n");
                return;
            }
            beApply.getFriendRequest().put(playerId, request);
            playerData.updatePlayer(beApply);
        } else {
            TPlayer tPlayer = playerDAO.findByRoleId(applyId);
            Map<Long, TPlayerInfo> map = JSON.parseObject(tPlayer.getFriendRequest(), new TypeReference<Map<Long, TPlayerInfo>>() {
            });
            if (map.containsKey(playerId)) {
                notifyScene.notifyPlayer(player, "已添加过了哦!\n");
                return;
            }
            map.put(playerId, request);
            tPlayer.setFriendRequest(JSON.toJSONString(map));
            playerDAO.save(tPlayer);
        }
        notifyScene.notifyPlayer(player, "发送请求\n");
    }

    /**
     * 同意好友请求
     */
    public void acceptFriend(Player player, long friendId) {
        long playerId = player.getTPlayer().getRoleId();
        if (!player.getFriendRequest().containsKey(friendId)) {
            notifyScene.notifyPlayer(player, "没有收到该玩家的请求\n");
            return;
        }
        player.getFriend().put(friendId, player.getFriendRequest().get(friendId));
        player.asynchronousSubmitEvent(new FriendAddEvent());
        player.getFriendRequest().remove(friendId);
        playerData.updatePlayer(player);

        TPlayerInfo otherAdd = playerManager.getPlayerInfoMap().get(playerId).gettPlayerInfo();
        if (userMap.getPlayers().containsKey(friendId)) {
            Player friend = userMap.getPlayers(friendId);
            friend.getFriend().put(playerId, otherAdd);
            friend.asynchronousSubmitEvent(new FriendAddEvent());
            playerData.updatePlayer(friend);
            notifyScene.notifyPlayer(friend, MessageFormat.format("[{0}]{1}同意您的好友请求\n", playerId, player.getName()));
        } else {
            TPlayer tPlayer = playerDAO.findByRoleId(friendId);
            Player friend = new Player();
            friend.setTPlayer(tPlayer);
            friend.setFriend(JSON.parseObject(tPlayer.getFriend(), new TypeReference<Map<Long, TPlayerInfo>>() {
            }));
            friend.setQuestMap(JSON.parseObject(tPlayer.getQuest(), new TypeReference<Map<QuestState, List<Integer>>>() {
            }));
            friend.setOnDoingQuest(JSON.parseObject(tPlayer.getOnDoingQuest(), new TypeReference<Map<QuestType, Map<Integer, Quest>>>() {
            }));
            friend.getFriend().put(playerId, otherAdd);
            friend.asynchronousSubmitEvent(new FriendAddEvent());
            userMap.getPlayers().put(friendId, friend);
            playerMap.getPlayerLastUpdate().put(friendId, System.currentTimeMillis());
            playerData.updatePlayer(friend);
        }
        notifyScene.notifyPlayer(player, "添加成功\n");
    }


    public void refuseFriend(Player player, long friendId) {
        player.getFriendRequest().remove(friendId);
        playerData.updatePlayer(player);
        notifyScene.notifyPlayer(player, "拒绝成功\n");
    }

}
