package com.function.sect.model;

import com.alibaba.fastjson.JSON;
import com.function.item.model.Item;
import com.jpa.entity.TSect;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;

/**
 * @author Catherine
 * @create 2020-09-05 02:10
 */
@SuppressWarnings("rawtypes")
public class Sect {

    private TSect tSect;

    private List<Long> members = new CopyOnWriteArrayList<>();

    private List<Long> joinRequest = new CopyOnWriteArrayList<>();
    private Map<Integer, Item> wareHouse = new ConcurrentHashMap<>();
    private ScheduledFuture update;

    public Sect(TSect tSect) {
        this.tSect = tSect;
    }

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

    public List<Long> getJoinRequest() {
        return joinRequest;
    }

    public void setJoinRequest(List<Long> joinRequest) {
        this.joinRequest = joinRequest;
    }

    public Map<Integer, Item> getWareHouse() {
        return wareHouse;
    }

    public void setWareHouse(Map<Integer, Item> wareHouse) {
        this.wareHouse = wareHouse;
    }

    public ScheduledFuture getUpdate() {
        return update;
    }

    public void setUpdate(ScheduledFuture update) {
        this.update = update;
    }

    public void toJson() {
        tSect.setMember(JSON.toJSONString(members));
        tSect.setWarehouse(JSON.toJSONString(wareHouse));
        tSect.setJoinRequest(JSON.toJSONString(joinRequest));

    }
}
