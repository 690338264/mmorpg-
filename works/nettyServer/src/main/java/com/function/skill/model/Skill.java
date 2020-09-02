package com.function.skill.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.function.buff.model.Buff;
import com.function.skill.excel.SkillExcel;
import com.function.skill.excel.SkillResource;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Catherine
 * @create 2020-07-31 12:43
 */
@Data
@JsonIgnoreProperties(value = {}, ignoreUnknown = true)
public class Skill {

    private Integer skillId;

    private List<Buff> buffList = new ArrayList<>();

    public SkillExcel getSkillExcel() {
        return SkillResource.getSkillById(skillId);
    }
}
