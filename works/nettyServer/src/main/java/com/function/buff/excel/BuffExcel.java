package com.function.buff.excel;

import lombok.Data;

/**
 * @author Catherine
 * @create 2020-08-25 11:34
 */
@Data
public class BuffExcel {

    private int id;

    private String name;

    private int type;

    private String hp;

    private String atk;
    /**
     * 状态持续时间
     */
    private long last;
    /**
     * 状态执行次数
     */
    private int times;
    /**
     * 目标类型（敌方/己方）
     */
    private int targetType;
    /**
     * 目标数量
     */
    private int targetNumber;

    private String describe;

    private double rate;

    private Integer summon;
}
