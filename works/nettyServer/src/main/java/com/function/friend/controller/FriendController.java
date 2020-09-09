package com.function.friend.controller;

import com.Cmd;
import com.function.friend.service.FriendService;
import com.function.player.model.Player;
import com.handler.ControllerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.Msg;
import util.ParamNumCheck;

/**
 * @author Catherine
 * @create 2020-09-09 17:36
 */
@Component
public class FriendController {
    @Autowired
    private FriendService friendService;

    {
        ControllerManager.add(Cmd.LIST_FRIEND, this::listFriends);
        ControllerManager.add(Cmd.ADD_FRIEND, this::addFriend);
        ControllerManager.add(Cmd.ACCEPT_FRIEND, this::acceptFriend);
        ControllerManager.add(Cmd.REFUSE_FRIEND, this::refuseFriend);
    }

    private void listFriends(Player player, Msg msg) {
        friendService.listFriends(player);
    }

    private void addFriend(Player player, Msg msg) {
        String[] params = ParamNumCheck.numCheck(player, msg, 2);
        long addId = Long.parseLong(params[1]);
        friendService.sendApply(player, addId);
    }

    private void acceptFriend(Player player, Msg msg) {
        String[] params = ParamNumCheck.numCheck(player, msg, 2);
        long requestId = Long.parseLong(params[1]);
        friendService.acceptFriend(player, requestId);
    }

    private void refuseFriend(Player player, Msg msg) {
        String[] params = ParamNumCheck.numCheck(player, msg, 2);
        long requestId = Long.parseLong(params[1]);
        friendService.refuseFriend(player, requestId);
    }

}
