package com.function.occ.excel;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Catherine
 */
@Data
public class OccExcel {

    private Integer id;

    private String name;

    private Integer hp;

    private Integer mp;
    /**
     * 门派攻击力
     */
    private Integer atk;
    /**
     * 门派防御力
     */
    private Integer def;
    /**
     * 门派速度
     */
    private Integer speed;
    /**
     * 门派技能
     */
    private String skill;
    /**
     * 升级倍数
     */
    private Integer multiple;

    private List<Integer> skillId = new ArrayList<>();

}
