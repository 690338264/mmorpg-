package com.event.model;

import com.event.QuestEvent;
import com.function.quest.model.QuestType;

/**
 * @author Catherine
 * @create 2020-09-13 19:01
 */
public class ItemGetEvent extends QuestEvent {
    @Override
    public QuestType getType() {
        return QuestType.ITEM_GET;
    }

    public ItemGetEvent(int itemId, int param) {
        this.itemId = itemId;
        this.param = param;
    }

    private final int itemId;

    private final int param;

    public int getItemId() {
        return itemId;
    }

    public int getParam() {
        return param;
    }
}