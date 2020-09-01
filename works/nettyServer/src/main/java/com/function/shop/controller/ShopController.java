package com.function.shop.controller;

import com.Cmd;
import com.function.player.model.Player;
import com.function.shop.service.ShopService;
import com.function.user.service.UserService;
import com.handler.ControllerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.Msg;
import util.ParamNumCheck;

/**
 * @author Catherine
 * @create 2020-08-19 16:10
 */
@Component
public class ShopController {
    @Autowired
    private UserService userService;
    @Autowired
    private ShopService shopService;

    {
        ControllerManager.add(Cmd.SHOW_SHOP, this::showShop);
        ControllerManager.add(Cmd.BUY, this::buyGood);
        ControllerManager.add(Cmd.SELL, this::sellGood);
    }

    private void showShop(Player player, Msg msg) {
        shopService.show(player);
    }

    private void buyGood(Player player, Msg msg) {
        String[] params = ParamNumCheck.numCheck(player, msg, 3);
        int itemId = Integer.parseInt(params[1]);
        int num = Integer.parseInt(params[2]);
        shopService.buy(player, itemId, num);
    }

    private void sellGood(Player player, Msg msg) {
        String[] params = ParamNumCheck.numCheck(player, msg, 3);
        int index = Integer.parseInt(params[1]);
        int num = Integer.parseInt(params[2]);
        shopService.sell(player, index, num);
    }


}
