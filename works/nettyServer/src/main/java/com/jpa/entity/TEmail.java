package com.jpa.entity;

import javax.persistence.*;

/**
 * @author Catherine
 * @create 2020-09-02 15:54
 */
@Entity
@Table(name = "email")
public class TEmail {
    @Id
    @GeneratedValue
    @Column(name = "emailId")
    private Long emailId;
    @Column(name = "playerId")
    private Long playerId;

    @Column(name = "sender")
    private Long sender;

    @Column(name = "text")
    private String text;

    @Column(name = "gift")
    private String gift;

    @Column(name = "state")
    private int state;

    public Long getEmailId() {
        return emailId;
    }

    public void setEmailId(Long emailId) {
        this.emailId = emailId;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public Long getSender() {
        return sender;
    }

    public void setSender(Long sender) {
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getGift() {
        return gift;
    }

    public void setGift(String gift) {
        this.gift = gift;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "TEmail{" +
                "emailId=" + emailId +
                ", playerId=" + playerId +
                ", sender=" + sender +
                ", text='" + text + '\'' +
                ", gift='" + gift + '\'' +
                ", state=" + state +
                '}';
    }
}
