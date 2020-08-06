package com.function.scene.model;

import com.function.scene.excel.SceneExcel;
import com.function.scene.excel.SceneResource;
import lombok.Data;

/**
 * @author Catherine
 * @create 2020-08-06 10:31
 */

@Data
public class Scene {
    private int sceneId;

    public SceneExcel getSceneExcel() {
        return SceneResource.getSceneById(sceneId);
    }
}
