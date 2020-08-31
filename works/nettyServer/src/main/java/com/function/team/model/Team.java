package com.function.team.model;

import com.function.player.model.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Catherine
 * @create 2020-08-24 14:52
 */
public class Team {
    /**
     * 队长id
     */
    private Long leaderId;
    /**
     * 最大成员数量
     */
    private int max = 10;
    /**
     * 副本Id
     */
    private Integer instanceId;
    /**
     * 小队成员
     */
    private Map<Long, Player> members = new ConcurrentHashMap<>();
    /**
     * 发送邀请
     */
    private Map<Long, Player> invite = new ConcurrentHashMap<>();
    /**
     * 申请加入
     */
    private Map<Long, Player> apply = new ConcurrentHashMap<>();

    public Long getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(Long leaderId) {
        this.leaderId = leaderId;
    }

    public int getMax() {
        return max;
    }

    public int getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(int instanceId) {
        this.instanceId = instanceId;
    }

    public Map<Long, Player> getMembers() {
        return members;
    }

    public Map<Long, Player> getInvite() {
        return invite;
    }

    public Map<Long, Player> getApply() {
        return apply;
    }
}
