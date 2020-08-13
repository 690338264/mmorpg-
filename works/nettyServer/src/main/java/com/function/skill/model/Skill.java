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
    /**
     * 技能上次释放时间
     */
    private Long lastTime;

    public SkillExcel getSkillExcel() {
        return SkillResource.getSkillById(skillId);
    }
}
