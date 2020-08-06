package com.function.scene.excel;

import com.function.monster.model.Monster;
import com.function.player.model.PlayerModel;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Catherine
 */
@Data
public class SceneExcel {

    private Integer id;

    private String name;
    /**
     * 相邻场景
     */
    private String neighbor;
    /**
     * 场景内的NPC
     */
    private String npc;
    /**
     * 场景内的怪物
     */
    private String monster;
    /**
     * 场景内玩家
     */
    private Map<Long, PlayerModel> players = new HashMap<>();
    /**
     * 场景内怪物
     */
    private Map<Integer, Monster> monsters = new HashMap<>();


}
