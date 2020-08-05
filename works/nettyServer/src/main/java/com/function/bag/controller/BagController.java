package com.function.bag.controller;

import com.Cmd;
import com.function.bag.service.BagService;
import com.function.player.model.PlayerModel;
import com.function.user.service.UserService;
import com.handler.ControllerManager;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.Msg;

@Component
public class BagController {

    @Autowired
    private UserService userService;
    @Autowired
    private BagService bagservice;

    {
        ControllerManager.add(Cmd.BAG_LIST, this::baglist);
        ControllerManager.add(Cmd.BAG_INORDER, this::baginOrder);
    }

    private void baglist(ChannelHandlerContext ctx, Msg msg) {
        PlayerModel playerModel = userService.getPlayerByCtx(ctx);
        bagservice.listBag(playerModel, ctx);

    }

    private void baginOrder(ChannelHandlerContext ctx, Msg msg) {
        PlayerModel playerModel = userService.getPlayerByCtx(ctx);
        bagservice.orderBag(playerModel, playerModel.getBagModel().getItemMap());
    }

}
