package com.function.skill.map;

import com.function.skill.model.Skill;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Catherine
 * @create 2020-08-25 16:30
 */
@Component
public class SkillMap {
    private Map<Integer, Skill> skillCache = new HashMap<>();

    public Skill get(Integer k) {
        return skillCache.get(k);
    }

    public void put(Integer k, Skill v) {
        skillCache.put(k, v);
    }

    public Map<Integer, Skill> getSkillCache() {
        return skillCache;
    }
}
