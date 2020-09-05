package com.function.sect.controller;

import com.Cmd;
import com.function.player.model.Player;
import com.function.sect.service.SectService;
import com.handler.ControllerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.Msg;
import util.ParamNumCheck;

/**
 * @author Catherine
 * @create 2020-09-05 02:17
 */
@Component
public class SectController {
    @Autowired
    private SectService sectService;

    {
        ControllerManager.add(Cmd.CREATE_SECT, this::createSect);
        ControllerManager.add(Cmd.REQUEST_JOIN, this::requestJoin);
        ControllerManager.add(Cmd.ACCEPT_SECT, this::acceptRequest);
        ControllerManager.add(Cmd.REJECT_SECT, this::reject);
        ControllerManager.add(Cmd.CHECK_SECT, this::checkSect);
        ControllerManager.add(Cmd.SET_POSITION, this::setPosition);
        ControllerManager.add(Cmd.GET_WAREHOUSE, this::getItem);
        ControllerManager.add(Cmd.PUT_WAREHOUSE, this::donateItem);
        ControllerManager.add(Cmd.QUIT_SECT, this::quitSect);
        ControllerManager.add(Cmd.LIST_SECT, this::listSect);
    }

    private void createSect(Player player, Msg msg) {
        String[] params = ParamNumCheck.numCheck(player, msg, 2);
        String name = params[1];
        sectService.createSect(player, name);
    }

    private void requestJoin(Player player, Msg msg) {
        String[] params = ParamNumCheck.numCheck(player, msg, 2);
        long sectId = Long.parseLong(params[1]);
        sectService.joinRequest(player, sectId);
    }

    private void checkSect(Player player, Msg msg) {
        sectService.checkSect(player);
    }

    private void acceptRequest(Player player, Msg msg) {
        String[] params = ParamNumCheck.numCheck(player, msg, 2);
        long playerId = Long.parseLong(params[1]);
        sectService.agreeJoin(player, playerId);
    }

    private void reject(Player player, Msg msg) {
        String[] params = ParamNumCheck.numCheck(player, msg, 2);
        long playerId = Long.parseLong(params[1]);
        sectService.rejectJoin(player, playerId);
    }

    private void setPosition(Player player, Msg msg) {
        String[] params = ParamNumCheck.numCheck(player, msg, 3);
        long playerId = Long.parseLong(params[1]);
        int position = Integer.parseInt(params[2]);
        sectService.setPosition(player, playerId, position);
    }

    private void donateItem(Player player, Msg msg) {
        String[] params = ParamNumCheck.numCheck(player, msg, 3);
        int index = Integer.parseInt(params[1]);
        int num = Integer.parseInt(params[2]);
        sectService.donateItem(player, index, num);
    }

    private void getItem(Player player, Msg msg) {
        String[] params = ParamNumCheck.numCheck(player, msg, 3);
        int index = Integer.parseInt(params[1]);
        int num = Integer.parseInt(params[2]);
        sectService.getFromSect(player, index, num);
    }

    private void quitSect(Player player, Msg msg) {
        sectService.quitSect(player);
    }

    private void listSect(Player player, Msg msg) {
        sectService.listSect(player);
    }

}
