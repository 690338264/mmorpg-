package com.function.scene.excel;

import lombok.Data;

import java.util.List;

/**
 * @author Catherine
 */
@Data
public class SceneExcel {

    private int id;

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
     * 场景类型
     */
    private int type;
    /**
     * 副本销毁时间
     */
    private Long destroy;

    private List<Integer> monsters;

    private List<Integer> npcs;

    private List<Integer> neighbors;


}
