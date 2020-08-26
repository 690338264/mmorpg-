package com.function.scene.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    private int sceneId;

    private Map<String, SceneObject> sceneObjectMap = new HashMap<>();

    public SceneExcel getSceneExcel() {
        return SceneResource.getSceneById(sceneId);
    }
}
