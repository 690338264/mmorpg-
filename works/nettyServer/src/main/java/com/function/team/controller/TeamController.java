package com.function.team.controller;

import com.Cmd;
import com.function.player.model.Player;
import com.function.team.service.TeamService;
import com.function.user.service.UserService;
import com.handler.ControllerManager;
import io.netty.channel.ChannelHandlerContext;
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
    @Autowired
    private UserService userService;

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

    private void listTeams(ChannelHandlerContext ctx, Msg msg) {
        Player player = userService.getPlayerByCtx(ctx);
        teamService.listTeams(player);
    }

    private void createTeam(ChannelHandlerContext ctx, Msg msg) {
        Player player = userService.getPlayerByCtx(ctx);
        teamService.createTeam(player);
    }

    private void checkTeam(ChannelHandlerContext ctx, Msg msg) {
        Player player = userService.getPlayerByCtx(ctx);
        teamService.checkTeam(player);
    }

    private void applyAdd(ChannelHandlerContext ctx, Msg msg) {
        Player player = userService.getPlayerByCtx(ctx);
        String[] params = ParamNumCheck.numCheck(ctx, msg, 2);
        Long teamId = Long.parseLong(params[1]);
        teamService.applyTeam(player, teamId);
    }

    private void invitePlayer(ChannelHandlerContext ctx, Msg msg) {
        Player player = userService.getPlayerByCtx(ctx);
        String[] params = ParamNumCheck.numCheck(ctx, msg, 2);
        Long playerId = Long.parseLong(params[1]);
        teamService.invitePlayer(player, playerId);
    }

    private void applyAgree(ChannelHandlerContext ctx, Msg msg) {
        Player player = userService.getPlayerByCtx(ctx);
        String[] params = ParamNumCheck.numCheck(ctx, msg, 2);
        Long playerId = Long.parseLong(params[1]);
        teamService.agreeApply(player, playerId);
    }

    private void acceptInvite(ChannelHandlerContext ctx, Msg msg) {
        Player player = userService.getPlayerByCtx(ctx);
        String[] params = ParamNumCheck.numCheck(ctx, msg, 2);
        Long teamId = Long.parseLong(params[1]);
        teamService.accept(player, teamId);
    }

    private void leave(ChannelHandlerContext ctx, Msg msg) {
        Player player = userService.getPlayerByCtx(ctx);
        teamService.leaveTeam(player);
    }
}
