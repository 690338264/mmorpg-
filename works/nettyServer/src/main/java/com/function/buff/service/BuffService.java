package com.function.buff.service;

import com.function.scene.model.SceneObject;
import org.springframework.stereotype.Component;

/**
 * @author Catherine
 * @create 2020-08-27 18:01
 */
@Component
public class BuffService {

    /**
     * 移除Buff
     */
    public void removeBuff(SceneObject sceneObject) {
        sceneObject.getBuffs().forEach((k, v) -> {
            v.cancel(true);
            sceneObject.getBuffs().remove(k);
        });
    }

}
