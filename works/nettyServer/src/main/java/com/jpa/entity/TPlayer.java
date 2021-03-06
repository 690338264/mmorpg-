package com.jpa.entity;

import javax.persistence.*;

/**
 * @author Catherine
 * @create 2020-08-11 20:34
 */
@Entity
@Table(name = "player")
public class TPlayer {

    @Column(name = "userId")
    private Long userId;

    @Column(name = "name")
    private String name;

    @Column(name = "exp")
    private int exp;

    @Column(name = "loc")
    private int loc;

    @Column(name = "occupation")
    private int occupation;

    @Column(name = "equip", columnDefinition = "TEXT")
    private String equip;

    @Column(name = "money")
    private int money;

    @Column(name = "friend", columnDefinition = "TEXT")
    private String friend = "{}";

    @Id
    @GeneratedValue
    @Column(name = "roleId")
    private Long roleId;

    @Column(name = "level")
    private int level;

    @Column(name = "sectId")
    private Long sectId;

    @Column(name = "sectPosition")
    private Integer sectPosition;

    @Column(name = "friendRequest")
    private String friendRequest = "{}";

    @Column(name = "onDoingQuest", columnDefinition = "TEXT")
    private String onDoingQuest;

    @Column(name = "quest", columnDefinition = "TEXT")
    private String quest;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getLoc() {
        return loc;
    }

    public void setLoc(int loc) {
        this.loc = loc;
    }

    public int getOccupation() {
        return occupation;
    }

    public void setOccupation(int occupation) {
        this.occupation = occupation;
    }

    public String getEquip() {
        return equip;
    }

    public void setEquip(String equip) {
        this.equip = equip;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getFriend() {
        return friend;
    }

    public void setFriend(String friend) {
        this.friend = friend;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Long getSectId() {
        return sectId;
    }

    public void setSectId(Long sectId) {
        this.sectId = sectId;
    }

    public int getSectPosition() {
        return sectPosition;
    }

    public void setSectPosition(int sectPosition) {
        this.sectPosition = sectPosition;
    }

    public String getFriendRequest() {
        return friendRequest;
    }

    public void setFriendRequest(String friendRequest) {
        this.friendRequest = friendRequest;
    }

    public String getOnDoingQuest() {
        return onDoingQuest;
    }

    public void setOnDoingQuest(String onDoingQuest) {
        this.onDoingQuest = onDoingQuest;
    }

    public String getQuest() {
        return quest;
    }

    public void setQuest(String quest) {
        this.quest = quest;
    }

    @Override
    public String toString() {
        return "TPlayer{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", exp=" + exp +
                ", loc=" + loc +
                ", occupation=" + occupation +
                ", equip='" + equip + '\'' +
                ", money=" + money +
                ", friend='" + friend + '\'' +
                ", roleId=" + roleId +
                ", level=" + level +
                ", sectId=" + sectId +
                ", sectPosition=" + sectPosition +
                ", friendRequest='" + friendRequest + '\'' +
                ", onDoingQuest='" + onDoingQuest + '\'' +
                ", quest='" + quest + '\'' +
                '}';
    }
}
