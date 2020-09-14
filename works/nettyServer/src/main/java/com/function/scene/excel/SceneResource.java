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

            sceneExcel.setMonsters(stringToList(sceneExcel.getMonster()));

            sceneExcel.setNpcs(stringToList(sceneExcel.getNpc()));
        }
        sceneMap.get(SceneType.PUBLIC.getType()).forEach((sceneId, sceneExcel) -> {
            Scene scene = sceneManagerCache.createScene(SceneType.PUBLIC.getType(), sceneId, sceneId);
            sceneExcel.setNeighbors(stringToList(sceneExcel.getNeighbor()));
            IntStream.range(0, sceneExcel.getMonsters().size()).forEach(i -> sceneManagerCache.createMonster(scene, i));
            sceneManagerCache.createNpc(scene);
            sceneManagerCache.publicStart(scene);
        });

    }

    public List<Integer> stringToList(String string) {
        String[] strs = string.split(",");
        int[] str = Arrays.stream(strs).mapToInt(Integer::parseInt).toArray();
        return Arrays.stream(str).boxed().collect(Collectors.toList());
    }

    public static Map<Integer, SceneExcel> getSceneMap(int type) {
        return sceneMap.get(type);
    }

    public static SceneExcel getSceneById(int type, int id) {
        return sceneMap.get(type).get(id);
    }
}
