package com.function.bag.controller;

import com.Cmd;
import com.function.bag.service.BagService;
import com.function.player.model.Player;
import com.handler.ControllerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.Msg;

/**
 * @author Catherine
 */
@Component
public class BagController {

    @Autowired
    private BagService bagservice;

    {
        ControllerManager.add(Cmd.BAG_LIST, this::bagList);
        ControllerManager.add(Cmd.BAG_INORDER, this::bagInorder);
    }

    private void bagList(Player player, Msg msg) {
        bagservice.listBag(player);

    }

    private void bagInorder(Player player, Msg msg) {
        bagservice.orderBag(player, player.getBag().getItemMap());
    }

}
