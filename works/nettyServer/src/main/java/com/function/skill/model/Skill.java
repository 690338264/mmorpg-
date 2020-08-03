package com.function.skill.model;

import com.function.skill.excel.SkillExcel;
import com.function.skill.excel.SkillResource;
import lombok.Data;

/**
 * @author Catherine
 * @create 2020-07-31 12:43
 */
@Data
public class Skill {
    private Integer skillId;
    private Long lastTime;
    private Long nowTime;

    public SkillExcel getSkillExcel() {
        return SkillResource.getSkillById(skillId);
    }
}
