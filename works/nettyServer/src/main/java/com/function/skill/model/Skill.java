package com.function.skill.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.function.buff.model.Buff;
import com.function.skill.excel.SkillExcel;
import com.function.skill.excel.SkillResource;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * @author Catherine
 * @create 2020-07-31 12:43
 */
@Data
@JsonIgnoreProperties(value = {}, ignoreUnknown = true)
public class Skill {

    private Integer skillId;

    private Map<Integer, Buff> buffMap = new HashMap<>();

    private Map<String, ScheduledFuture> taskMap = new ConcurrentHashMap<>();

    public SkillExcel getSkillExcel() {
        return SkillResource.getSkillById(skillId);
    }
}
