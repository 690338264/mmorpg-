package com.function.skill.excel;

import com.function.buff.excel.BuffResource;
import com.function.skill.cache.SkillCache;
import com.manager.ExcelManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    @Autowired
    private SkillCache skillCache;
    @Autowired
    private BuffResource buffResource;

    public static String className = "Skill";

    private static Map<Integer, SkillExcel> skillMap = new HashMap<>();

    @PostConstruct
    private void init() {
        int num = excelManager.getMap().get(className).size();
        for (int i = 0; i < num; i++) {
            SkillExcel skillExcel = (SkillExcel) excelManager.getMap().get(className).get(i);
            skillMap.put(skillExcel.getId(), skillExcel);
            if (skillExcel.getBuff() != null) {
                String[] buffs = skillExcel.getBuff().split(",");
                Arrays.asList(buffs).stream().forEach(buff -> skillExcel.getBuffId().add(Integer.parseInt(buff)));
            }
        }
    }

    public static SkillExcel getSkillById(int id) {
        return skillMap.get(id);
    }
}
