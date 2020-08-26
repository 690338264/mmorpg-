package com.function.scene.excel;

import com.function.monster.model.Monster;
import com.function.npc.excel.NpcResource;
import com.function.scene.manager.SceneMap;
import com.function.scene.model.Scene;
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
public class SceneResource {

    @Autowired
    private ExcelManager excelManager;
    @Autowired
    private SceneMap sceneMapCache;

    public static String Monster = "Monster";

    public static String Npc = "Npc";

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
            Scene scene = new Scene();
            scene.setSceneId(sceneExcel.getId());
            for (int j = 0; j < strs.length; j++) {
                int monsterId = Integer.parseInt(strs[j]);
                Monster monster = new Monster();
                monster.setId(monsterId);
                monster.setSceneId(j);
                monster.setHp(monster.getMonsterExcel().getHp());
                monster.setAtk(monster.getMonsterExcel().getAggr());
                monster.getCanUseSkill().putAll(monster.getMonsterExcel().getMonsterSkill());
                monster.setType(SceneObjectType.MONSTER.getType());
                scene.getSceneObjectMap().put(Monster + j, monster);
            }
            for (int j = 0; j < npcs.length; j++) {
                int npcId = Integer.parseInt(npcs[j]);
                scene.getSceneObjectMap().put(Npc + j, NpcResource.getNpcById(npcId));
            }
            sceneMapCache.getSceneCache().put(sceneExcel.getId(), scene);
        }

    }

    public static SceneExcel getSceneById(int id) {
        return sceneMap.get(id);
    }
}
