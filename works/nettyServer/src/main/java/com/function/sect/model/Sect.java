package com.function.sect.model;

import com.function.player.model.Player;
import com.jpa.entity.TSect;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Catherine
 * @create 2020-09-05 02:10
 */
public class Sect {
    private TSect tSect;
    private List<Long> members = new CopyOnWriteArrayList<>();
    private Map<Long, Player> joinRequest = new ConcurrentHashMap<>();

    public TSect gettSect() {
        return tSect;
    }

    public void settSect(TSect tSect) {
        this.tSect = tSect;
    }

    public List<Long> getMembers() {
        return members;
    }

    public void setMembers(List<Long> members) {
        this.members = members;
    }

    public Map<Long, Player> getJoinRequest() {
        return joinRequest;
    }

    public void setJoinRequest(Map<Long, Player> joinRequest) {
        this.joinRequest = joinRequest;
    }
}
