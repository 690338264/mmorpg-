package com.function.skill.excel;

import com.manager.ExcelManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.excel.ClassName;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Catherine
 */
@Component
public class SkillResource {
    @Autowired
    private ExcelManager excelManager;

    private static final Map<Integer, SkillExcel> skillMap = new HashMap<>();

    @PostConstruct
    private void init() {
        int num = excelManager.getMap().get(ClassName.Skill.name()).size();
        for (int i = 0; i < num; i++) {
            SkillExcel skillExcel = (SkillExcel) excelManager.getMap().get(ClassName.Skill.name()).get(i);
            skillMap.put(skillExcel.getId(), skillExcel);
            String[] buffs = skillExcel.getBuff().split(",");
            Arrays.asList(buffs).forEach(buff -> skillExcel.getBuffId().add(Integer.parseInt(buff)));
        }
    }

    public static SkillExcel getSkillById(int id) {
        return skillMap.get(id);
    }
}
