package com.function.quest.excel;

/**
 * @author Catherine
 * @create 2020-09-11 11:46
 */
public class QuestExcel {

    private int id;

    private String name;

    private String text;

    private int times;

    private int type;
    /**
     * 目标
     */
    private String target;
    /**
     * 前置任务
     */
    private Integer preQuest;

    private int item;

    private int money;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Integer getPreQuest() {
        return preQuest;
    }

    public void setPreQuest(Integer preQuest) {
        this.preQuest = preQuest;
    }

    public int getItem() {
        return item;
    }

    public void setItem(int item) {
        this.item = item;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
