package com.function.skill.excel;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Catherine
 */
@Data
public class SkillExcel {

    private int id;

    private String name;
    /**
     * 技能消耗mp
     */
    private int mp;

    private int atk;

    private long cd;

    private String buff;

    private List<Integer> buffId = new ArrayList<>();
}
