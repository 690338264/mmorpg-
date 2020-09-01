package com.function.skill.excel;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

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

    private Long cd;

    private String buff;

    private List<Integer> buffId = new ArrayList<>();
}
