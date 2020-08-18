package com.function.scene.manager;

import com.function.scene.model.Scene;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Catherine
 * @create 2020-08-13 15:56
 */
@Component
public class SceneMap {
    private Map<Integer, Scene> sceneCache = new HashMap<>();

    public Map<Integer, Scene> getSceneCache() {
        return sceneCache;
    }
}
