package com.function.item.controller;

import com.Cmd;
import com.function.item.model.Item;
import com.function.item.service.ItemService;
import com.function.player.model.Player;
import com.function.scene.service.NotifyScene;
import com.function.user.service.UserService;
import com.handler.ControllerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.Msg;
import util.ParamNumCheck;

import java.text.MessageFormat;


/**
 * @author Catherine
 */
@Component
public class ItemController {

    @Autowired
    private UserService userService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private NotifyScene notifyScene;

    {
        ControllerManager.add(Cmd.ITEM_USE, this::itemUse);
        ControllerManager.add(Cmd.ITEM_DROP, this::itemDrop);
        ControllerManager.add(Cmd.EQUIP_OFF, this::equipOff);
        ControllerManager.add(Cmd.EQUIP_LIST, this::equipList);
        ControllerManager.add(Cmd.EQUIP_FIX, this::equipFix);
    }

    private void itemUse(Player player, Msg msg) {
        String[] params = ParamNumCheck.numCheck(player, msg, 2);
        int index = Integer.parseInt(params[1]);
        itemService.useItem(index, player);
    }

    private void itemDrop(Player player, Msg msg) {
        String[] params = ParamNumCheck.numCheck(player, msg, 3);
        int index = Integer.parseInt(params[1]);
        int num = Integer.parseInt(params[2]);
        Item item = player.getBag().getItemMap().get(index);
        String name = item.getItemById().getName();
        Long id = item.getItemId();
        if (itemService.removeItem(id, index, num, player)) {
            notifyScene.notifyPlayer(player, MessageFormat.format("您已丢弃:[{0}]\n", name));

        }
    }

    private void equipOff(Player player, Msg msg) {
        String[] params = ParamNumCheck.numCheck(player, msg, 2);
        int index = Integer.parseInt(params[1]);
        itemService.removeEquip(index, player);
    }

    private void equipList(Player player, Msg msg) {
        itemService.listEquip(player);
    }

    private void equipFix(Player player, Msg msg) {
        String[] params = ParamNumCheck.numCheck(player, msg, 2);
        int index = Integer.parseInt(params[1]);
        itemService.fixEquip(player, index);
    }

}
