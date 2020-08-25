package com.function.occ.excel;

import com.function.occ.manager.OccCache;
import com.function.skill.cache.SkillCache;
import com.function.skill.model.Skill;
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
public class OccResource {

    @Autowired
    private ExcelManager excelManager;
    @Autowired
    private OccCache occCache;
    @Autowired
    private SkillCache skillCache;


    private static Map<Integer, OccExcel> occupationMap = new HashMap<>();

    @PostConstruct
    private void init() {
        int num = excelManager.getMap().get("Occ").size();
        for (int i = 0; i < num; i++) {
            OccExcel occExcel = (OccExcel) excelManager.getMap().get("Occ").get(i);
            occupationMap.put(occExcel.getId(), occExcel);
            String[] occSkill = occExcel.getSkill().split(",");
            for (int j = 0; j < occSkill.length; j++) {
                int skillId = Integer.parseInt(occSkill[j]);
                Skill skill = skillCache.get("Skill" + skillId);
                occExcel.getSkills().add(skill);
            }
            occCache.set("Occ" + occExcel.getId(), occExcel);
        }

    }

    public static OccExcel getOccById(int id) {
        return occupationMap.get(id);
    }
}
