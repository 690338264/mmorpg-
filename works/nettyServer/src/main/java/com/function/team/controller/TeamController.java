package com.function.team.controller;

import com.Cmd;
import com.function.player.model.Player;
import com.function.team.service.TeamService;
import com.handler.ControllerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.Msg;
import util.ParamNumCheck;

/**
 * @author Catherine
 * @create 2020-08-24 15:07
 */
@Component
public class TeamController {
    @Autowired
    private TeamService teamService;

    {
        ControllerManager.add(Cmd.LIST_TEAM, this::listTeams);
        ControllerManager.add(Cmd.CREATE_TEAM, this::createTeam);
        ControllerManager.add(Cmd.CHECK_TEAM, this::checkTeam);
        ControllerManager.add(Cmd.APPLY_TEAM, this::applyAdd);
        ControllerManager.add(Cmd.INVITE_PLAYER, this::invitePlayer);
        ControllerManager.add(Cmd.AGREE_APPLY, this::applyAgree);
        ControllerManager.add(Cmd.ACCEPT_TEAM, this::acceptInvite);
        ControllerManager.add(Cmd.LEAVE_TEAM, this::leave);
    }

    private void listTeams(Player player, Msg msg) {
        teamService.listTeams(player);
    }

    private void createTeam(Player player, Msg msg) {
        teamService.createTeam(player);
    }

    private void checkTeam(Player player, Msg msg) {
        teamService.checkTeam(player);
    }

    private void applyAdd(Player player, Msg msg) {
        String[] params = ParamNumCheck.numCheck(player, msg, 2);
        Long teamId = Long.parseLong(params[1]);
        teamService.applyTeam(player, teamId);
    }

    private void invitePlayer(Player player, Msg msg) {
        String[] params = ParamNumCheck.numCheck(player, msg, 2);
        Long playerId = Long.parseLong(params[1]);
        teamService.invitePlayer(player, playerId);
    }

    private void applyAgree(Player player, Msg msg) {
        String[] params = ParamNumCheck.numCheck(player, msg, 2);
        Long playerId = Long.parseLong(params[1]);
        teamService.agreeApply(player, playerId);
    }

    private void acceptInvite(Player player, Msg msg) {
        String[] params = ParamNumCheck.numCheck(player, msg, 2);
        Long teamId = Long.parseLong(params[1]);
        teamService.accept(player, teamId);
    }

    private void leave(Player player, Msg msg) {
        teamService.leaveTeam(player);
    }
}
