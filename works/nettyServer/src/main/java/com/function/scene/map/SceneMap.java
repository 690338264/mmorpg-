package com.function.scene.map;

import com.function.scene.model.Scene;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class SceneMap {
    private static Map<Integer, Scene> sceneMap = new HashMap<Integer, Scene>();
    @PostConstruct
    public void init(){

    }



}
