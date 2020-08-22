package com.function.communicate.model;

import com.function.item.model.Item;
import com.function.player.model.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Catherine
 * @create 2020-08-20 20:21
 */
public class Email {

    private Player sender;

    private String text;

    private int state;

    private Map<Integer, Item> gifts = new HashMap<>();

    public Email() {
    }

    public Email(Player sender, String text) {
        this.sender = sender;
        this.text = text;
        this.state = 0;
    }

    public Player getSender() {
        return sender;
    }

    public void setSender(Player sender) {
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Map<Integer, Item> getGifts() {
        return gifts;
    }

    public void setGifts(Map<Integer, Item> gifts) {
        this.gifts = gifts;
    }
}
