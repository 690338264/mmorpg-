package com.function.player.model;

import org.springframework.stereotype.Component;
import util.excel.ExcelUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OccResource {
    private static Map<Integer, OccExcel> occupationMap = new HashMap<Integer, OccExcel>();

    @PostConstruct
    private void init(){
        File file = new File("C:\\Users\\Dell\\Desktop\\mmorpg-\\works\\nettyServer\\src\\main\\resources\\excels\\occupation.xlsx");
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            List<OccExcel> list = ExcelUtils.readExcelToEntity(OccExcel.class, in, file.getName());
            for(int i = 0;i<list.size();i++) {
                occupationMap.put(list.get(i).getId(),list.get(i));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static OccExcel getOccById(int id) {
        return occupationMap.get(id);
    }
}
