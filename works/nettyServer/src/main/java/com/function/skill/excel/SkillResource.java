package com.function.skill.excel;

import com.function.buff.model.Buff;
import com.function.skill.cache.SkillCache;
import com.function.skill.model.Skill;
import com.manager.ExcelManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * @author Catherine
 */
@Component
public class SkillResource {
    @Autowired
    private ExcelManager excelManager;
    @Autowired
    private SkillCache skillCache;

    public static String className = "Skill";

    private static Map<Integer, SkillExcel> skillMap = new HashMap<>();

    @PostConstruct
    private void init() {
        int num = excelManager.getMap().get(className).size();
        for (int i = 0; i < num; i++) {
            SkillExcel skillExcel = (SkillExcel) excelManager.getMap().get(className).get(i);
            skillMap.put(skillExcel.getId(), skillExcel);
            Skill skill = new Skill();
            skill.setSkillId(skillExcel.getId());
            if (skillExcel.getBuff() != null) {
                String[] buffs = skillExcel.getBuff().split(",");
                IntStream.range(0, buffs.length).forEach(j -> {
                    Buff buff = new Buff();
                    int buffId = Integer.parseInt(buffs[j]);
                    buff.setId(buffId);
                    skill.getBuffMap().put(j, buff);
                });
            }
            skillCache.set(className + skill.getSkillId(), skill);
        }
    }

    public static SkillExcel getSkillById(int id) {
        return skillMap.get(id);
    }
}
