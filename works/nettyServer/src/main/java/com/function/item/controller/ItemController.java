package com.function.item.controller;

import com.Cmd;
import com.function.item.service.ItemService;
import com.function.player.model.Player;
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
        ControllerManager.add(Cmd.ITEM_DROP, this::itemDrop);
        ControllerManager.add(Cmd.EQUIP_OFF, this::equipOff);
        ControllerManager.add(Cmd.EQUIP_LIST, this::equipList);
    }

    private void itemUse(ChannelHandlerContext ctx, Msg msg) {
        Player player = userService.getPlayerByCtx(ctx);
        String[] params = ParamNumCheck.numCheck(ctx, msg, 2);
        Integer index = Integer.valueOf(params[1]);
        itemService.useItem(index, player, ctx);
    }

    private void equipOn(ChannelHandlerContext ctx, Msg msg) {
        Player player = userService.getPlayerByCtx(ctx);
        String[] params = ParamNumCheck.numCheck(ctx, msg, 2);
        Integer index = Integer.parseInt(params[1]);
        itemService.wearEquipment(index, player, ctx);
    }

    private void itemDrop(ChannelHandlerContext ctx, Msg msg) {
        Player player = userService.getPlayerByCtx(ctx);
        String[] params = ParamNumCheck.numCheck(ctx, msg, 3);
        Integer index = Integer.parseInt(params[1]);
        Integer num = Integer.parseInt(params[2]);
        String name = player.getBag().getItemMap().get(index).getItemById().getName();
        itemService.removeItem(index, num, player);
        ctx.writeAndFlush("您已丢弃:[" + name + "]\n");
    }

    private void equipOff(ChannelHandlerContext ctx, Msg msg) {
        Player player = userService.getPlayerByCtx(ctx);
        String[] params = ParamNumCheck.numCheck(ctx, msg, 2);
        Integer index = Integer.parseInt(params[1]);
        itemService.removeEquip(index, player, ctx);
    }

    private void equipList(ChannelHandlerContext ctx, Msg msg) {
        Player player = userService.getPlayerByCtx(ctx);
        itemService.listEquip(player, ctx);
    }
}
