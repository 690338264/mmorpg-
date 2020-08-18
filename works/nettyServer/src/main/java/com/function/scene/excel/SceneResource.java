package com.function.scene.excel;

import com.function.monster.model.Monster;
import com.function.npc.excel.NpcResource;
import com.function.scene.manager.SceneMap;
import com.function.scene.model.Scene;
import com.manager.ExcelManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

/**
 * @author Catherine
 */
@Component
public class SceneResource {

    @Autowired
    private ExcelManager excelManager;
    @Autowired
    private SceneMap sceneMapCache;

    private static Map<Integer, SceneExcel> sceneMap = new HashMap<Integer, SceneExcel>();

    @PostConstruct
    private void init() {
        int num = excelManager.getMap().get("Scene").size();
        for (int i = 0; i < num; i++) {
            SceneExcel sceneExcel = (SceneExcel) excelManager.getMap().get("Scene").get(i);
            sceneMap.put(sceneExcel.getId(), sceneExcel);
            String str = sceneExcel.getMonster();
            String[] strs = str.split(",");
            String[] npcs = sceneExcel.getNpc().split(",");

            for (int j = 0; j < strs.length; j++) {
                int monsterId = Integer.parseInt(strs[j]);
                Monster monster = new Monster();
                monster.setId(monsterId);
                monster.setSceneId(j);
                monster.setSelfHp(monster.getMonsterExcel().getHp());
                monster.getCanUseSkill().putAll(monster.getMonsterExcel().getMonsterSkill());
                monster.setTimer(new Timer());
                sceneExcel.getMonsters().put(j, monster);
            }
            for (int j = 0; j < npcs.length; j++) {
                int npcId = Integer.parseInt(npcs[j]);
                sceneExcel.getNpcs().put(j, NpcResource.getNpcById(npcId));
            }
            Scene scene = new Scene();
            scene.setSceneId(sceneExcel.getId());
//            sceneCache.set(scene);
            sceneMapCache.getSceneCache().put(sceneExcel.getId(), scene);
        }

    }

    public static SceneExcel getSceneById(int id) {
        return sceneMap.get(id);
    }
}
