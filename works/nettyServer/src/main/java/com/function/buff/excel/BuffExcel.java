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

    private int hp;

    private int atk;
    /**
     * 状态持续时间
     */
    private long last;
    /**
     * 状态执行次数
     */
    private int times;
    /**
     * 正/负面状态
     */
    private int state;
}
