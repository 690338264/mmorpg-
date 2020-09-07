package com.function.team.model;

import com.function.player.model.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * @author Catherine
 * @create 2020-08-24 14:52
 */
@SuppressWarnings("rawtypes")
public class Team {
    /**
     * 队长id
     */
    private Long leaderId;
    /**
     * 小队成员
     */
    private final Map<Long, Player> members = new ConcurrentHashMap<>();
    /**
     * 发送邀请
     */
    private final Map<Long, Long> invite = new ConcurrentHashMap<>();
    /**
     * 申请加入
     */
    private final Map<Long, Long> apply = new ConcurrentHashMap<>();
    /**
     * 处理入队邀请和请求
     */
    private ScheduledFuture scheduledFuture;

    public Long getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(Long leaderId) {
        this.leaderId = leaderId;
    }

    public Map<Long, Player> getMembers() {
        return members;
    }

    public Map<Long, Long> getInvite() {
        return invite;
    }

    public Map<Long, Long> getApply() {
        return apply;
    }

    public ScheduledFuture getScheduledFuture() {
        return scheduledFuture;
    }

    public void setScheduledFuture(ScheduledFuture scheduledFuture) {
        this.scheduledFuture = scheduledFuture;
    }
}
