package com.function.scene.excel;

import lombok.Data;

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
     * 场景类型
     */
    private int type;


}
