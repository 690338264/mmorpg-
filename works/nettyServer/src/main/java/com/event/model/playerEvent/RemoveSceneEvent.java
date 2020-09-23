package com.event.model.playerEvent;

import com.event.BasePlayerEvent;
import com.function.scene.model.Scene;

/**
 * @author Catherine
 * @create 2020-09-20 17:47
 */
public class RemoveSceneEvent extends BasePlayerEvent {
    private final Scene oldScene;

    public RemoveSceneEvent(Scene oldScene) {
        this.oldScene = oldScene;
    }

    public Scene getOldScene() {
        return oldScene;
    }
}
