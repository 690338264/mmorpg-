package com.function.scene.model;

import com.function.monster.model.Monster;
import com.function.scene.excel.SceneExcel;
import com.function.scene.excel.SceneResource;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * @author Catherine
 * @create 2020-08-06 10:31
 */

@Data
@SuppressWarnings("rawtypes")
public class Scene {

    private int id;

    private int sceneId;

    private int type;

    private Map<Integer, Map<Long, SceneObject>> sceneObjectMap = new ConcurrentHashMap<>();

    private Map<Long, Monster> waitForRevive = new ConcurrentHashMap<>();

    private ScheduledFuture heartBeat;

    public SceneExcel getSceneExcel() {
        return SceneResource.getSceneById(type, sceneId);
    }
}
