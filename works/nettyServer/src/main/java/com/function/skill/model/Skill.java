package com.function.skill.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.function.buff.model.Buff;
import com.function.skill.excel.SkillExcel;
import com.function.skill.excel.SkillResource;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
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

    private Map<Integer, Buff> buffMap = new HashMap<>();

    public SkillExcel getSkillExcel() {
        return SkillResource.getSkillById(skillId);
    }
}
