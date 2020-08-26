package com.function.npc.excel;

import com.function.scene.model.SceneObjectType;
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
public class NpcResource {

    @Autowired
    private ExcelManager excelManager;

    private static Map<Integer, NpcExcel> npcMap = new HashMap<>();

    @PostConstruct
    private void init() {
        int num = excelManager.getMap().get("Npc").size();
        for (int i = 0; i < num; i++) {
            NpcExcel npcExcel = (NpcExcel) excelManager.getMap().get("Npc").get(i);
            npcExcel.setType(SceneObjectType.NPC.getType());
            npcMap.put(npcExcel.getId(), npcExcel);
        }
    }

    public static NpcExcel getNpcById(int id) {
        return npcMap.get(id);
    }
}
