package com.function.scene.manager;

import com.function.monster.service.MonsterService;
import com.function.scene.model.Scene;
import com.function.scene.model.SceneType;
import com.manager.ThreadPoolManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Catherine
 * @create 2020-08-13 15:56
 */
@Component
public class SceneManager {
    @Autowired
    private MonsterService monsterService;
    public static int jump = 1000;

    private Map<Integer, Map<Integer, Scene>> scene = new HashMap<>();

    @PostConstruct
    public void init() {
        for (SceneType s : SceneType.values()) {
            scene.put(s.getType(), new HashMap<>());
        }
    }

    public void put(int type, int sceneId, Scene s) {
        scene.get(type).put(sceneId, s);
        ThreadPoolManager.loopThread(() -> {
            monsterService.monsterRevive(s);
        }, 0, jump, sceneId);
    }

    public Map<Integer, Scene> get(int type) {
        return scene.get(type);
    }


}
