package com.function.email.model;

import com.alibaba.fastjson.JSON;
import com.function.item.model.Item;
import com.jpa.entity.TEmail;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

/**
 * @author Catherine
 * @create 2020-09-02 16:19
 */
@SuppressWarnings("rawtypes")
public class Email {

    private TEmail tEmail;
    /**
     * 礼物列表
     */
    private List<Item> gifts = new ArrayList<>();

    private ScheduledFuture update;

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

    public ScheduledFuture getUpdate() {
        return update;
    }

    public void setUpdate(ScheduledFuture update) {
        this.update = update;
    }

    public void toJson() {
        String json = JSON.toJSONString(gifts);
        tEmail.setGift(json);
    }

    @Override
    public String toString() {
        return "Email{" +
                "tEmail=" + tEmail +
                ", gifts=" + gifts +
                ", update=" + update +
                '}';
    }
}
