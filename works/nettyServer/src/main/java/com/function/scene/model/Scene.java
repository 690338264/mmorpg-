package com.function.scene.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.function.monster.model.Monster;
import com.function.scene.excel.SceneExcel;
import com.function.scene.excel.SceneResource;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Catherine
 * @create 2020-08-06 10:31
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Scene {

    private int id;

    private int sceneId;

    private Map<Integer, Map<Long, SceneObject>> sceneObjectMap = new HashMap<>();

    private Map<Long, Monster> waitForRevive = new HashMap<>();

    public SceneExcel getSceneExcel(int type) {
        return SceneResource.getSceneById(type, sceneId);
    }
}
