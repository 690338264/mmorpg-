package com.function.monster.model;

import com.function.npc.model.Npc;
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
    private static Map<Integer,Monster> monsterMap = new HashMap<Integer, Monster>();
    @PostConstruct
    private void init(){
        File file = new File("C:\\Users\\Dell\\Desktop\\mmorpg-\\works\\nettyServer\\src\\main\\resources\\excels\\monster.xlsx");
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            List<Monster> list = ExcelUtils.readExcelToEntity(Monster.class,in,file.getName());
            for(int i = 0;i<list.size();i++) {
                monsterMap.put(list.get(i).getId(),list.get(i));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static Monster getMonById(int id){
        return monsterMap.get(id);
    }
}
