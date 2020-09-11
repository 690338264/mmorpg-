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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "emailId")
    private Long emailId;
    @Column(name = "playerId")
    private long playerId;

    @Column(name = "sender")
    private Long sender;

    @Column(name = "text", columnDefinition = "TEXT")
    private String text;

    @Column(name = "gift", columnDefinition = "TEXT")
    private String gift;

    @Column(name = "state")
    private String state;

    public Long getEmailId() {
        return emailId;
    }

    public void setEmailId(Long emailId) {
        this.emailId = emailId;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
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
