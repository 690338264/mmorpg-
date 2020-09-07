package com.function.item.excel;

import lombok.Data;

/**
 * @author Catherine
 * @create 2020-08-04 10:48
 */

@Data
public class ItemExcel {

    private int id;

    private String name;
    /**
     * 加血量
     */
    private int hp;
    /**
     * 加mp
     */
    private int mp;
    /**
     * 加攻击
     */
    private int atk;
    /**
     * 加防御
     */
    private int def;
    /**
     * 加速度
     */
    private int speed;
    /**
     * 交易价格
     */
    private int money;
    /**
     * 是药品还是装备
     */
    private int type;
    /**
     * 装备装饰的部位
     */
    private int space;
    /**
     * 耐久度
     */
    private int wear;
    /**
     * 最大堆叠数量
     */
    private int maxNum;

}
