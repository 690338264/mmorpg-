package com.function.skill.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.function.skill.excel.SkillExcel;
import com.function.skill.excel.SkillResource;
import lombok.Data;

import java.util.Timer;

/**
 * @author Catherine
 * @create 2020-07-31 12:43
 */
@Data
@JsonIgnoreProperties(value = {"timer"}, ignoreUnknown = true)
public class Skill {

    private Integer skillId;
    /**
     * 技能上次释放时间
     */
    private Long lastTime;

    private Timer timer;

    public SkillExcel getSkillExcel() {
        return SkillResource.getSkillById(skillId);
    }
}
