package com.function.skill.model;

import org.springframework.stereotype.Component;
import util.excel.ExcelUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author Catherine
 */
@Component
public class SkillResource {

    private static Map<Integer, Skill> skillMap = new HashMap<Integer, Skill>();

    @PostConstruct
    private void init(){
        File file = new File("C:\\Users\\Dell\\Desktop\\mmorpg-\\works\\nettyServer\\src\\main\\resources\\excels\\skill.xlsx");
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            List<Skill> list = ExcelUtils.readExcelToEntity(Skill.class,in,file.getName());
            for(int i = 0; i<list.size(); i++) {
                skillMap.put(list.get(i).getId(),list.get(i));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static Skill getSkillById(int id){
        return skillMap.get(id);
    }
}
