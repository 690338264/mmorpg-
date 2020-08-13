package com.function.scene.excel;

import com.function.monster.model.Monster;
import com.function.npc.excel.NpcExcel;
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
     * 场景内怪物
     */
    private Map<Integer, Monster> monsters = new HashMap<>();
    /**
     * 场景内Npc
     */
    private Map<Integer, NpcExcel> npcs = new HashMap<>();


}
