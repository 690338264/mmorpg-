package com.function.summon.excel;

import com.function.skill.model.Skill;
import com.manager.ExcelManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.excel.ClassName;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Catherine
 * @create 2020-09-23 11:44
 */
@Component
public class SummonResource {
    @Autowired
    private ExcelManager excelManager;

    private static final Map<Integer, SummonExcel> summonMap = new HashMap<>();

    @PostConstruct
    private void init() {
        int num = excelManager.getMap().get(ClassName.Summon.name()).size();
        for (int i = 0; i < num; i++) {
            SummonExcel summon = (SummonExcel) excelManager.getMap().get(ClassName.Summon.name()).get(i);
            summonMap.put(summon.getId(), summon);
            String str = summon.getSkill();
            String[] strs = str.split(",");
            for (int j = 0; j < strs.length; j++) {
                int skillId = Integer.parseInt(strs[j]);
                Skill skill = new Skill(skillId);
                summon.getSummonSkill().put(j, skill);
            }
        }
    }

    public static SummonExcel getMonById(int id) {
        return summonMap.get(id);
    }
}
