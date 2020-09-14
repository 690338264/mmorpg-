package com.function.scene.excel;

import com.function.scene.manager.SceneManager;
import com.function.scene.model.Scene;
import com.function.scene.model.SceneType;
import com.manager.ExcelManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.excel.ClassName;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Catherine
 */
@Component
public class SceneResource {

    @Autowired
    private ExcelManager excelManager;
    @Autowired
    private SceneManager sceneManagerCache;

    public static int idTimes = 1000000;

    private static final Map<Integer, Map<Integer, SceneExcel>> sceneMap = new HashMap<>();

    @PostConstruct
    private void init() {
        int num = excelManager.getMap().get(ClassName.Scene.name()).size();
        for (SceneType s : SceneType.values()) {
            sceneMap.put(s.getType(), new HashMap<>());
        }

        for (int i = 0; i < num; i++) {
            SceneExcel sceneExcel = (SceneExcel) excelManager.getMap().get(ClassName.Scene.name()).get(i);
            sceneMap.get(sceneExcel.getType()).put(sceneExcel.getId(), sceneExcel);
            String str = sceneExcel.getMonster();

            String[] strs = str.split(",");
            int[] monsters = Arrays.stream(strs).mapToInt(Integer::parseInt).toArray();
            List<Integer> monster = Arrays.stream(monsters).boxed().collect(Collectors.toList());
            sceneExcel.setMonsters(monster);

            String[] split = sceneExcel.getNpc().split(",");
            int[] npcs = Arrays.stream(split).mapToInt(Integer::parseInt).toArray();
            List<Integer> npc = Arrays.stream(npcs).boxed().collect(Collectors.toList());
            sceneExcel.setNpcs(npc);
        }
        sceneMap.get(SceneType.PUBLIC.getType()).forEach((k, v) -> {
            Scene scene = sceneManagerCache.createScene(SceneType.PUBLIC.getType(), k);
            IntStream.range(0, v.getMonsters().size()).forEach(i -> sceneManagerCache.createMonster(scene, i));
            sceneManagerCache.createNpc(scene);
            sceneManagerCache.publicStart(scene);
        });

    }

    public static Map<Integer, SceneExcel> getSceneMap(int type) {
        return sceneMap.get(type);
    }

    public static SceneExcel getSceneById(int type, int id) {
        return sceneMap.get(type).get(id);
    }
}
