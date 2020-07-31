package com.function.skill.excel;

import com.manager.ExcelManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Catherine
 */
@Component
public class SkillResource {
    @Autowired
    private ExcelManager excelManager;

    private static Map<Integer, SkillExcel> skillMap = new HashMap<Integer, SkillExcel>();

    @PostConstruct
    private void init() {
        int num = excelManager.getMap().get("Skill").size();
        for (int i = 0; i < num; i++) {
            SkillExcel skill = (SkillExcel) excelManager.getMap().get("Skill").get(i + 1);
            skillMap.put(skill.getId(), skill);
        }
    }

    public static SkillExcel getSkillById(int id) {
        return skillMap.get(id);
    }
}
