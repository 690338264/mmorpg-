package com.function.monster.model;

import com.function.skill.excel.SkillExcel;
import com.function.skill.excel.SkillResource;
import com.manager.ExcelManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class MonsterResource {
    @Autowired
    private SkillResource skillResource;

    private static Map<Integer, MonsterExcel> monsterMap = new HashMap<Integer, MonsterExcel>();

    @PostConstruct
    private void init() {
        ExcelManager excelManager = new ExcelManager();
        int num = excelManager.getMap().get("Monster").size();
        for (int i = 0; i < num; i++) {
            MonsterExcel monster = (MonsterExcel) excelManager.getMap().get("Monster").get(i);
            monsterMap.put(monster.getId(), monster);
            String str = monster.getSkill();
            String[] strs = str.split(",");
            for (int j = 0; j < strs.length; j++) {
                int skillId = Integer.parseInt(strs[j]);
                SkillExcel skillExcel = SkillResource.getSkillById(skillId);
                monster.getMonsterSkill().put(j, skillExcel);
            }
        }
//        File file = new File("C:\\Users\\Dell\\Desktop\\mmorpg-\\works\\nettyServer\\src\\main\\resources\\excels\\monster.xlsx");
//        FileInputStream in = null;
//        try {
//            in = new FileInputStream(file);
//            List<MonsterExcel> list = ExcelUtils.readExcelToEntity(MonsterExcel.class, in, file.getName());
//            for (int i = 0; i < list.size(); i++) {
//                monsterMap.put(list.get(i).getId(),list.get(i));
//                String str = list.get(i).getSkill();
//                String[] strs = str.split(",");
//                for (int j = 0; j < strs.length; j++) {
//                    int skillId = Integer.parseInt(strs[j]);
//                    SkillExcel skillExcel = SkillResource.getSkillById(skillId);
//                    list.get(i).getMonsterSkill().put(j, skillExcel);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public static MonsterExcel getMonById(int id) {
        return monsterMap.get(id);
    }
}
