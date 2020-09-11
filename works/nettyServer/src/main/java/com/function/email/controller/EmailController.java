package com.function.email.controller;

import com.Cmd;
import com.function.email.service.EmailService;
import com.function.player.model.Player;
import com.function.scene.service.NotifyScene;
import com.handler.ControllerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.Msg;
import util.ParamNumCheck;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author Catherine
 * @create 2020-09-02 15:45
 */
@Component
public class EmailController {
    @Autowired
    private EmailService emailService;
    @Autowired
    private NotifyScene notifyScene;

    {
        ControllerManager.add(Cmd.LIST_EMAIL, this::showEmail);
        ControllerManager.add(Cmd.CHECK_EMAIL, this::showDetail);
        ControllerManager.add(Cmd.SEND_EMAIL, this::sendEmail);
        ControllerManager.add(Cmd.RECEIVE_EMAIL, this::receiveGift);
    }

    private void sendEmail(Player player, Msg msg) {
        String[] params = msg.getContent().split(" ");
        if (params.length == 3) {
            Long playerId = Long.parseLong(params[1]);
            String text = params[2];
            emailService.sendOnlyText(player, playerId, text);
        } else if (params.length == 5) {
            Long playerId = Long.parseLong(params[1]);
            String text = params[2];
            String[] items = params[3].split(",");
            String[] num = params[4].split(",");
            List<Integer> indexs = new ArrayList<>();
            List<Integer> itemNum = new ArrayList<>();
            IntStream.range(0, items.length).forEach(i -> {
                if (Integer.parseInt(num[i]) > 0) {
                    indexs.add(Integer.parseInt(items[i]));
                    itemNum.add(Integer.parseInt(num[i]));
                }
            });
            emailService.sendHasGift(player, playerId, text, indexs, itemNum);
        } else {
            notifyScene.notifyPlayer(player, "参数数目错误!\n");
        }
    }

    private void showEmail(Player player, Msg msg) {
        emailService.showEmail(player);
    }

    private void showDetail(Player player, Msg msg) {
        String[] params = ParamNumCheck.numCheck(player, msg, 2);
        int emailId = Integer.parseInt(params[1]);
        emailService.showDetail(player, emailId);
    }

    private void receiveGift(Player player, Msg msg) {
        String[] params = ParamNumCheck.numCheck(player, msg, 2);
        int emailId = Integer.parseInt(params[1]);
        emailService.receive(player, emailId);
    }
}
