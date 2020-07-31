package com.function.scene.excel;

import com.function.monster.model.Monster;
import com.manager.ExcelManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class SceneResource {

    @Autowired
    private ExcelManager excelManager;

    private static Map<Integer, SceneExcel> sceneMap = new HashMap<Integer, SceneExcel>();

    @PostConstruct
    private void init() {
        int num = excelManager.getMap().get("Scene").size();
        for (int i = 0; i < num; i++) {
            SceneExcel sceneExcel = (SceneExcel) excelManager.getMap().get("Scene").get(i + 1);
            sceneMap.put(sceneExcel.getId(), sceneExcel);
            String str = sceneExcel.getMonster();
            String[] strs = str.split(",");
            for (int j = 0; j < strs.length; j++) {
                int monsterId = Integer.parseInt(strs[j]);
                Monster monster = new Monster();
                monster.setId(monsterId);
                monster.setSceneId(j);
                monster.setStatus(1);
                monster.setSelfHp(monster.getMonsterExcel().getHp());
                sceneExcel.getMonsters().put(j, monster);
            }
        }

    }

    public static SceneExcel getSceneById(int id) {
        return sceneMap.get(id);
    }
}
