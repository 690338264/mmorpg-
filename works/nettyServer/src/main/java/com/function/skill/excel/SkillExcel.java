package com.function.skill.excel;

import lombok.Data;

/**
 * @author Catherine
 */
@Data
public class SkillExcel {

    private Integer id;

    private String name;
    /**
     * 技能消耗mp
     */
    private Integer mp;

    private Integer atk;

    private Integer cd;

    private String buff;
}
