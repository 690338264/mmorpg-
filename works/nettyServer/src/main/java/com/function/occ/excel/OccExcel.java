package com.function.occ.excel;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Catherine
 */
@Data
public class OccExcel {

    private int id;

    private String name;

    private int hp;

    private int mp;
    /**
     * 门派攻击力
     */
    private int atk;
    /**
     * 门派防御力
     */
    private int def;
    /**
     * 门派速度
     */
    private int speed;
    /**
     * 门派技能
     */
    private String skill;
    /**
     * 升级倍数
     */
    private int multiple;

    private List<Integer> skillId = new ArrayList<>();

}
