package com.function.skill.excel;

import com.function.buff.map.BuffMap;
import com.function.skill.map.SkillMap;
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
    private SkillMap skillCache;
    @Autowired
    private BuffMap buffCache;

    private static Map<Integer, SkillExcel> skillMap = new HashMap<>();

    @PostConstruct
    private void init() {
        int num = excelManager.getMap().get("Skill").size();
        for (int i = 0; i < num; i++) {
            SkillExcel skillExcel = (SkillExcel) excelManager.getMap().get("Skill").get(i);
            skillMap.put(skillExcel.getId(), skillExcel);
            Skill skill = new Skill();
            skill.setSkillId(skillExcel.getId());
            if (skillExcel.getBuff() != null) {
                String[] buffs = skillExcel.getBuff().split(",");
                IntStream.range(0, buffs.length).forEach(j -> {
                    int buffId = Integer.parseInt(buffs[j]);
                    skill.getBuffMap().put(j, buffCache.get(buffId));
                });
            }
            skillCache.put(skill.getSkillId(), skill);
        }
    }

    public static SkillExcel getSkillById(int id) {
        return skillMap.get(id);
    }
}
