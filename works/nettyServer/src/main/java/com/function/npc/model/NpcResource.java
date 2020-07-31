package com.function.npc.model;

import org.springframework.stereotype.Component;
import util.excel.ExcelUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class NpcResource {
    private static Map<Integer, NpcExcel> npcMap = new HashMap<Integer, NpcExcel>();

    @PostConstruct
    private void init() {
        File file = new File("C:\\Users\\Dell\\Desktop\\mmorpg-\\works\\nettyServer\\src\\main\\resources\\excels\\npc.xlsx");
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            List<NpcExcel> list = ExcelUtils.readExcelToEntity(NpcExcel.class, in, file.getName());
            for (int i = 0; i < list.size(); i++) {
                npcMap.put(list.get(i).getId(), list.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static NpcExcel getNpcById(int id) {
        return npcMap.get(id);
    }
}
