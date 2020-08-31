package com.function.scene.manager;

import com.function.monster.model.Monster;
import com.function.monster.service.MonsterService;
import com.function.npc.excel.NpcResource;
import com.function.scene.excel.SceneExcel;
import com.function.scene.excel.SceneResource;
import com.function.scene.model.Instance;
import com.function.scene.model.Scene;
import com.function.scene.model.SceneObjectType;
import com.function.scene.model.SceneType;
import com.function.scene.service.InstanceService;
import com.function.scene.service.NotifyScene;
import com.manager.ThreadPoolManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

/**
 * @author Catherine
 * @create 2020-08-13 15:56
 */
@Component
public class SceneManager {
    @Autowired
    private MonsterService monsterService;
    @Autowired
    private InstanceService instanceService;
    @Autowired
    private NotifyScene notifyScene;

    public static int jump = 1000;
    public static String heartBeat = "heartBeat";

    private Map<Integer, Map<Integer, Scene>> scene = new HashMap<>();

    @PostConstruct
    public void init() {
        for (SceneType s : SceneType.values()) {
            scene.put(s.getType(), new HashMap<>());
        }
    }

    /**
     * 场景加载完成
     */
    public void start(Scene s) {
        ThreadPoolManager.loopThread(() -> monsterService.monsterRevive(s), 0, jump, s.getId());
    }

    public void starts(int sceneId, Instance instance) {
        Scene s = instance.getScene();
        ScheduledFuture scheduledFuture = ThreadPoolManager.loopThread(() -> {
            if (instance.getScene().getSceneObjectMap().get(SceneObjectType.MONSTER.getType()).isEmpty()) {
                if (!instanceService.nextBoss(instance)) {
                    notifyScene.notifyScene(s, "副本挑战成功,即将关闭!\n");
                    instanceService.destroy(instance);
                    return;
                }
            }
            if (System.currentTimeMillis() - instance.getCreateTime() > s.getSceneExcel().getDestroy()) {
                notifyScene.notifyScene(s, "副本挑战失败,即将关闭!\n");
                instanceService.destroy(instance);
                return;
            }
        }, 0, jump, sceneId);
        s.getTaskMap().put(heartBeat, scheduledFuture);
    }

    public Map<Integer, Scene> get(int type) {
        return scene.get(type);
    }

    /**
     * 创建场景
     */
    public Scene createScene(int type, int sceneId) {
        Scene s = new Scene();
        s.setId(sceneId * SceneResource.idTimes + scene.get(type).size());
        s.setSceneId(sceneId);
        s.setType(type);
        for (SceneObjectType object : SceneObjectType.values()) {
            s.getSceneObjectMap().put(object.getType(), new HashMap<>());
        }
        scene.get(type).put(s.getId(), s);
        return s;
    }

    /**
     * 创建怪物
     */
    public boolean createMonster(Scene s, int num) {
        if (num > s.getSceneExcel().getMonsters().length - 1) {
            return false;
        }
        int monsterId = Integer.parseInt(s.getSceneExcel().getMonsters()[num]);
        Monster monster = new Monster();
        monster.setId(monsterId);
        monster.setSceneId((long) num);
        monster.setHp(monster.getMonsterExcel().getHp());
        monster.setAtk(monster.getMonsterExcel().getAggr());
        monster.getCanUseSkill().putAll(monster.getMonsterExcel().getMonsterSkill());
        monster.setType(SceneObjectType.MONSTER.getType());
        s.getSceneObjectMap().get(SceneObjectType.MONSTER.getType()).put(monster.getSceneId(), monster);
        return true;
    }

    /**
     * 创建Npc
     */
    public void createNpc(Scene scene) {
        SceneExcel sceneExcel = scene.getSceneExcel();
        for (int j = 0; j < sceneExcel.getNpcs().length; j++) {
            int npcId = Integer.parseInt(sceneExcel.getNpcs()[j]);
            scene.getSceneObjectMap().get(SceneObjectType.NPC.getType()).put((long) npcId, NpcResource.getNpcById(npcId));
        }
    }

//    public Scene initObject(int type, int sceneId) {
//        SceneExcel sceneExcel = SceneResource.getSceneById(type, sceneId);
//        Scene s = new Scene();
//        s.setId(sceneId * SceneResource.idTimes + scene.get(type).size());
//        s.setSceneId(sceneId);
//        s.setType(type);
//        for (SceneObjectType object : SceneObjectType.values()) {
//            s.getSceneObjectMap().put(object.getType(), new HashMap<>());
//        }
//        for (int j = 0; j < sceneExcel.getMonsters().length; j++) {
//            int monsterId = Integer.parseInt(sceneExcel.getMonsters()[j]);
//            Monster monster = new Monster();
//            monster.setId(monsterId);
//            monster.setSceneId((long) j);
//            monster.setHp(monster.getMonsterExcel().getHp());
//            monster.setAtk(monster.getMonsterExcel().getAggr());
//            monster.getCanUseSkill().putAll(monster.getMonsterExcel().getMonsterSkill());
//            monster.setType(SceneObjectType.MONSTER.getType());
//            s.getSceneObjectMap().get(SceneObjectType.MONSTER.getType()).put(monster.getSceneId(), monster);
//        }
//        for (int j = 0; j < sceneExcel.getNpcs().length; j++) {
//            int npcId = Integer.parseInt(sceneExcel.getNpcs()[j]);
//            s.getSceneObjectMap().get(SceneObjectType.NPC.getType()).put((long) j, NpcResource.getNpcById(npcId));
//        }
//        scene.get(type).put(sceneId, s);
//        if (type == SceneType.PUBLIC.getType()) {
//            start(s.getId(), s);
//        } else {
//
//        }
//        return s;
//    }


}
