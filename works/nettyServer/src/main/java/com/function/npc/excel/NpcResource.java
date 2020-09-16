package com.function.npc.excel;

import com.function.scene.model.SceneObjectType;
import com.manager.ExcelManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.excel.ClassName;

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

    private static final Map<Integer, NpcExcel> npcMap = new HashMap<>();

    @PostConstruct
    private void init() {
        int num = excelManager.getMap().get(ClassName.Npc.name()).size();
        for (int i = 0; i < num; i++) {
            NpcExcel npcExcel = (NpcExcel) excelManager.getMap().get(ClassName.Npc.name()).get(i);
            npcExcel.setType(SceneObjectType.NPC);
            npcMap.put(npcExcel.getId().intValue(), npcExcel);
        }
    }

    public static NpcExcel getNpcById(int id) {
        return npcMap.get(id);
    }
}
