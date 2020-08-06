package com.function.monster.excel;

import com.function.skill.excel.SkillResource;
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
public class MonsterResource {
    @Autowired
    private SkillResource skillResource;
    @Autowired
    private ExcelManager excelManager;

    private static Map<Integer, MonsterExcel> monsterMap = new HashMap<Integer, MonsterExcel>();

    @PostConstruct
    private void init() {
        int num = excelManager.getMap().get("Monster").size();
        for (int i = 0; i < num; i++) {
            MonsterExcel monster = (MonsterExcel) excelManager.getMap().get("Monster").get(i + 1);
            monsterMap.put(monster.getId(), monster);
            String str = monster.getSkill();
            String[] strs = str.split(",");
            for (int j = 0; j < strs.length; j++) {
                int skillId = Integer.parseInt(strs[j]);
                Skill skill = new Skill();
                skill.setSkillId(skillId);
                monster.getMonsterSkill().put(j, skill);
            }
        }
    }

    public static MonsterExcel getMonById(int id) {
        return monsterMap.get(id);
    }
}
