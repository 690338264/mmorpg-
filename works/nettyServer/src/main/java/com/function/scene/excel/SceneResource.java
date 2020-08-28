package com.function.scene.excel;

import com.function.monster.model.Monster;
import com.function.npc.excel.NpcResource;
import com.function.scene.manager.SceneManager;
import com.function.scene.model.Scene;
import com.function.scene.model.SceneObjectType;
import com.function.scene.model.SceneType;
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
    private SceneManager sceneManagerCache;

    public static String Monster = "Monster";

    public static String Npc = "Npc";

    public static int idTimes = 1000000;


    private static Map<Integer, Map<Integer, SceneExcel>> sceneMap = new HashMap<>();

    @PostConstruct
    private void init() {
        int num = excelManager.getMap().get("Scene").size();
        for (SceneType s : SceneType.values()) {
            sceneMap.put(s.getType(), new HashMap<>());
        }

        for (int i = 0; i < num; i++) {
            SceneExcel sceneExcel = (SceneExcel) excelManager.getMap().get("Scene").get(i);
            sceneMap.get(sceneExcel.getType()).put(sceneExcel.getId(), sceneExcel);
            String str = sceneExcel.getMonster();
            String[] strs = str.split(",");
            String[] npcs = sceneExcel.getNpc().split(",");
            if (sceneExcel.getType() != SceneType.PUBLIC.getType()) {
                continue;
            }
            Scene scene = new Scene();
            scene.setId(sceneExcel.getId() * idTimes);
            scene.setSceneId(sceneExcel.getId());
            for (SceneObjectType object : SceneObjectType.values()) {
                scene.getSceneObjectMap().put(object.getType(), new HashMap<>());
            }
            for (int j = 0; j < strs.length; j++) {
                int monsterId = Integer.parseInt(strs[j]);
                Monster monster = new Monster();
                monster.setId(monsterId);
                monster.setSceneId((long) j);
                monster.setHp(monster.getMonsterExcel().getHp());
                monster.setAtk(monster.getMonsterExcel().getAggr());
                monster.getCanUseSkill().putAll(monster.getMonsterExcel().getMonsterSkill());
                monster.setType(SceneObjectType.MONSTER.getType());
                scene.getSceneObjectMap().get(SceneObjectType.MONSTER.getType()).put(monster.getSceneId(), monster);
            }
            for (int j = 0; j < npcs.length; j++) {
                int npcId = Integer.parseInt(npcs[j]);
                scene.getSceneObjectMap().get(SceneObjectType.NPC.getType()).put((long) j, NpcResource.getNpcById(npcId));
            }
            sceneManagerCache.put(SceneType.PUBLIC.getType(), scene.getSceneId(), scene);
            System.out.println(sceneManagerCache.get(SceneType.PUBLIC.getType()));
        }

    }

    public static SceneExcel getSceneById(int type, int id) {
        return sceneMap.get(type).get(id);
    }
}
