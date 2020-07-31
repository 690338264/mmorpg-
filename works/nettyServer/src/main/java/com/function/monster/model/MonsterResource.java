package com.function.monster.model;

import com.function.skill.model.Skill;
import com.function.skill.model.SkillResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.excel.ExcelUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MonsterResource {
    @Autowired
    private SkillResource skillResource;

    private static Map<Integer, MonsterExcel> monsterMap = new HashMap<Integer, MonsterExcel>();

    @PostConstruct
    private void init() {
        File file = new File("C:\\Users\\Dell\\Desktop\\mmorpg-\\works\\nettyServer\\src\\main\\resources\\excels\\monster.xlsx");
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            List<MonsterExcel> list = ExcelUtils.readExcelToEntity(MonsterExcel.class, in, file.getName());
            for (int i = 0; i < list.size(); i++) {
                monsterMap.put(list.get(i).getId(),list.get(i));
                String str = list.get(i).getSkill();
                String[] strs = str.split(",");
                for (int j = 0; j < strs.length; j++) {
                    int skillId = Integer.parseInt(strs[j]);
                    Skill skill = SkillResource.getSkillById(skillId);
                    list.get(i).getMonsterSkill().put(j,skill);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static MonsterExcel getMonById(int id) {
        return monsterMap.get(id);
    }
}
