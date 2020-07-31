package com.function.npc.model;

import com.manager.ExcelManager;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class NpcResource {

    private static Map<Integer, NpcExcel> npcMap = new HashMap<Integer, NpcExcel>();

    @PostConstruct
    private void init() {
        ExcelManager excelManager = new ExcelManager();
        int num = excelManager.getMap().get("Npc").size();
        for (int i = 0; i < num; i++) {
            NpcExcel npcExcel = (NpcExcel) excelManager.getMap().get("Npc").get(i);
            npcMap.put(npcExcel.getId(), npcExcel);
        }
//        File file = new File("C:\\Users\\Dell\\Desktop\\mmorpg-\\works\\nettyServer\\src\\main\\resources\\excels\\npc.xlsx");
//        FileInputStream in = null;
//        try {
//            in = new FileInputStream(file);
//            List<NpcExcel> list = ExcelUtils.readExcelToEntity(NpcExcel.class, in, file.getName());
//            for (int i = 0; i < list.size(); i++) {
//                npcMap.put(list.get(i).getId(), list.get(i));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public static NpcExcel getNpcById(int id) {
        return npcMap.get(id);
    }
}
