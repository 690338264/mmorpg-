package com.function.team.service;

import com.event.model.TeamJoinEvent;
import com.function.occ.excel.OccExcel;
import com.function.occ.excel.OccResource;
import com.function.player.model.Player;
import com.function.scene.service.NotifyScene;
import com.function.team.manager.TeamManager;
import com.function.team.model.Team;
import com.function.user.map.UserMap;
import com.jpa.entity.TPlayer;
import com.manager.ThreadPoolManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Catherine
 * @create 2020-08-24 15:07
 */
@Component
@SuppressWarnings("rawtypes")
public class TeamService {
    @Autowired
    private TeamManager teamManager;
    @Autowired
    private NotifyScene notifyScene;
    @Autowired
    private UserMap userMap;

    public static Long inviteTime = 60000L;
    public static Long jump = 1000L;

    private static final int MAX = 10;
    private static final AtomicLong incTeamId = new AtomicLong();

    /**
     * 查看小队列表
     */
    public void listTeams(Player player) {
        for (Long key : teamManager.getTeamCache().keySet()) {
            Team team = teamManager.getTeamCache().get(key);
            notifyScene.notifyPlayer(player, MessageFormat.format("队伍ID:{0}   队伍人数:{1}\n",
                    key, team.getMembers().size()));
        }
    }

    /**
     * 创建小队
     */
    public void createTeam(Player player) {
        if (isInTeam(player)) {
            notifyScene.notifyPlayer(player, "您已在小队中，不可以创建小队！\n");
            return;
        }
        Team team = new Team();
        team.setLeaderId(player.getTPlayer().getRoleId());
        Long teamId = incTeamId.longValue();
        teamManager.getTeamCache().put(teamId, team);
        player.setTeamId(teamId);
        team.getMembers().put(player.getTPlayer().getRoleId(), player);
        addTeamId();
        ScheduledFuture scheduledFuture = ThreadPoolManager.loopThread(() -> {
            check(team.getApply());
            check(team.getInvite());
        }, 0, jump, teamId.intValue());
        team.setScheduledFuture(scheduledFuture);
        notifyScene.notifyPlayer(player, "小队创建成功!\n");
    }

    /**
     * 查看自己所在小队
     */
    public void checkTeam(Player player) {
        if (!isInTeam(player)) {
            notifyScene.notifyPlayer(player, "请先加入一支小队!\n");
            return;
        }
        Team team = teamManager.getTeamCache().get(player.getTeamId());
        Long leaderId = team.getLeaderId();
        notifyScene.notifyPlayer(player, MessageFormat.format("队伍id{0}  队长{1}\n",
                player.getTeamId(), team.getMembers().get(leaderId).getTPlayer().getName()));
        for (Long playerId : team.getMembers().keySet()) {
            Player member = team.getMembers().get(playerId);
            showMember(player, member);
        }
        if (leaderId.equals(player.getTPlayer().getRoleId())) {
            notifyScene.notifyPlayer(player, "申请列表:\n");
            for (Long key : team.getApply().keySet()) {
                Player apply = userMap.getPlayers(key);
                showMember(player, apply);
            }
        }
    }

    /**
     * 申请加入小队
     */
    public void applyTeam(Player player, Long teamId) {
        if (isInTeam(player)) {
            notifyScene.notifyPlayer(player, "您已加入一支小队\n");
            return;
        }
        Team team = teamManager.getTeamCache().get(teamId);
        if (team == null) {
            notifyScene.notifyPlayer(player, "小队不存在\n");
            return;
        }
        team.getApply().put(player.getTPlayer().getRoleId(), System.currentTimeMillis());
        Player leader = team.getMembers().get(team.getLeaderId());
        notifyScene.notifyPlayer(leader, "收到一条小队加入申请\n");
    }

    /**
     * 邀请玩家加入小队
     */
    public void invitePlayer(Player player, Long playerId) {
        if (!isInTeam(player)) {
            notifyScene.notifyPlayer(player, "您尚未加入小队\n");
            return;
        }
        Team team = teamManager.getTeamCache().get(player.getTeamId());
        Player invite = userMap.getPlayers(playerId);
        if (invite.getChannelHandlerContext() == null) {
            notifyScene.notifyPlayer(player, "玩家不在线！\n");
            return;
        }
        if (invite.getTeamId() != null) {
            notifyScene.notifyPlayer(player, "该玩家已在小队\n");
            return;
        }
        team.getInvite().put(playerId, System.currentTimeMillis());
        notifyScene.notifyPlayer(invite, "收到一条小队邀请\n");
        notifyScene.notifyPlayer(player, "要请发送成功\n");
    }

    /**
     * 同意入队申请
     */
    public void agreeApply(Player player, Long playerId) {
        Team team = teamManager.getTeamCache().get(player.getTeamId());
        if (!player.getTPlayer().getRoleId().equals(team.getLeaderId())) {
            notifyScene.notifyPlayer(player, "你还不是队长\n");
            return;
        }
        if (team.getApply().get(playerId) == null) {
            notifyScene.notifyPlayer(player, "玩家申请过时或未申请\n");
            return;
        }
        Player apply = userMap.getPlayers(playerId);
        if (addMember(apply, team)) {
            team.getApply().remove(playerId);
            notifyScene.notifyTeam(team, MessageFormat.format("[{0}]加入小队\n", apply.getTPlayer().getName()));
            team.getMembers().forEach((id, teamMate) ->
                    teamMate.submitEvent(new TeamJoinEvent()));
        }
    }

    /**
     * 接受小队的邀请
     */
    public void accept(Player player, Long teamId) {
        Team team = teamManager.getTeamCache().get(teamId);
        if (team == null) {
            notifyScene.notifyPlayer(player, "小队不存在\n");
            return;
        }
        if (team.getInvite().get(player.getTPlayer().getRoleId()) == null) {
            notifyScene.notifyPlayer(player, "邀请不存在\n");
            return;
        }
        if (addMember(player, team)) {
            team.getInvite().remove(player.getTPlayer().getRoleId());
            team.getMembers().forEach((id, teamMate) ->
                    teamMate.submitEvent(new TeamJoinEvent()));
            notifyScene.notifyTeam(team, MessageFormat.format("[{0}]加入小队\n", player.getTPlayer().getName()));
        }
    }

    /**
     * 离开小队
     */
    public void leaveTeam(Player player) {
        Long playerId = player.getTPlayer().getRoleId();
        if (!isInTeam(player)) {
            return;
        }
        Team team = teamManager.getTeamCache().get(player.getTeamId());
        if (team.getMembers().size() == 1) {
            team.getScheduledFuture().cancel(true);
            teamManager.getTeamCache().remove(player.getTeamId());
            player.setTeamId(null);
            notifyScene.notifyPlayer(player, "您已离开小队\n");
            return;
        }
        team.getMembers().remove(playerId);
        if (team.getLeaderId().equals(playerId)) {
            Long leader = team.getMembers().keySet().iterator().next();
            team.setLeaderId(leader);
        }
        player.setTeamId(null);
        notifyScene.notifyPlayer(player, "您已离开小队\n");
        notifyScene.notifyTeam(team, MessageFormat.format("{0}已离开小队\n", player.getTPlayer().getName()));
    }

    /**
     * 增加小队成员
     */
    public boolean addMember(Player player, Team team) {
        Player leader = team.getMembers().get(team.getLeaderId());
        if (player.getChannelHandlerContext() == null) {
            notifyScene.notifyPlayer(leader, "玩家已离线\n");
            return false;
        }
        if (team.getMembers().size() == MAX) {
            notifyScene.notifyPlayer(leader, "小队人已满\n");
            return false;
        }
        if (isInTeam(player)) {
            notifyScene.notifyPlayer(leader, "玩家已加入其它小队\n");
            return false;
        }
        player.setTeamId(leader.getTeamId());
        team.getMembers().put(player.getTPlayer().getRoleId(), player);
        return true;
    }

    /**
     * teamId
     */
    public void addTeamId() {
        incTeamId.getAndIncrement();
    }

    /**
     * 成员信息
     */
    public void showMember(Player player, Player member) {
        OccExcel occExcel = OccResource.getOccById(member.getTPlayer().getOccupation());
        TPlayer tPlayer = member.getTPlayer();
        notifyScene.notifyPlayer(player, MessageFormat.format("[{0}]  {1}  门派:{2}  等级{3}\n"
                , tPlayer.getRoleId(), tPlayer.getName(), occExcel.getName(), tPlayer.getLevel()));
    }

    /**
     * 是否在小队
     */
    public boolean isInTeam(Player player) {
        return player.getTeamId() != null;
    }

    public void check(Map<Long, Long> map) {
        map.forEach((k, v) -> {
            if (System.currentTimeMillis() - v > inviteTime) {
                map.remove(k);
            }
        });
    }
}
