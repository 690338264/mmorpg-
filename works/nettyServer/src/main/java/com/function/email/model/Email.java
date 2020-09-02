package com.function.email.model;

import com.function.item.model.Item;
import com.jpa.entity.TEmail;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Catherine
 * @create 2020-09-02 16:19
 */
public class Email {

    private TEmail tEmail;
    /**
     * 礼物列表
     */
    private List<Item> gifts = new ArrayList<>();

    public TEmail gettEmail() {
        return tEmail;
    }

    public void settEmail(TEmail tEmail) {
        this.tEmail = tEmail;
    }

    public List<Item> getGifts() {
        return gifts;
    }

    public void setGifts(List<Item> gifts) {
        this.gifts = gifts;
    }

    @Override
    public String toString() {
        return "Emails{" +
                "tEmail=" + tEmail +
                ", gifts=" + gifts +
                '}';
    }
}
