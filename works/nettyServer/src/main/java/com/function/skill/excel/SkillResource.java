package com.function.skill.excel;

import com.manager.ExcelManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
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
    private void init() throws FileNotFoundException {
        ExcelManager excelManager = new ExcelManager();
        //      excelManager.init();
        int num = excelManager.getMap().get("Skill").size();
        for (int i = 0; i < num; i++) {
            SkillExcel skill = (SkillExcel) excelManager.getMap().get("Skill").get(i);
            skillMap.put(skill.getId(), skill);
        }
//        File file = new File("C:\\Users\\Dell\\Desktop\\mmorpg-\\works\\nettyServer\\src\\main\\resources\\excels\\skill.xlsx");
//        FileInputStream in = null;
//        try {
//            in = new FileInputStream(file);
//            List<SkillExcel> list = ExcelUtils.readExcelToEntity(SkillExcel.class,in,file.getName());
//            for(int i = 0; i<list.size(); i++) {
//                skillMap.put(list.get(i).getId(),list.get(i));
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
    }

    public static SkillExcel getSkillById(int id) {
        return skillMap.get(id);
    }
}
