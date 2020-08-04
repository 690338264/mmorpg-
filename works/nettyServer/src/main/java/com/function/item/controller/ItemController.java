package com.function.item.controller;

import com.Cmd;
import com.function.item.service.ItemService;
import com.function.player.model.PlayerModel;
import com.function.user.service.UserService;
import com.handler.ControllerManager;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.Msg;
import util.ParamNumCheck;


/**
 * @author Catherine
 */
@Component
public class ItemController {

    @Autowired
    private UserService userService;
    @Autowired
    private ItemService itemService;

    {
        ControllerManager.add(Cmd.ITEM_USE, this::itemUse);
        ControllerManager.add(Cmd.EQUIP_ON, this::equipOn);
    }

    private void itemUse(ChannelHandlerContext ctx, Msg msg) {
        PlayerModel playerModel = userService.getPlayerByCtx(ctx);
        String[] params = ParamNumCheck.numCheck(ctx, msg, 2);
        Integer index = Integer.valueOf(params[1]);
        itemService.useItem(index, playerModel, ctx);
    }

    private void equipOn(ChannelHandlerContext ctx, Msg msg) {
        PlayerModel playerModel = userService.getPlayerByCtx(ctx);
        String[] params = ParamNumCheck.numCheck(ctx, msg, 2);
        Integer index = Integer.parseInt(params[1]);
        itemService.wearEquipment(index, playerModel, ctx);

    }
}
