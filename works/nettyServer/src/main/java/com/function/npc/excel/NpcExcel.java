package com.function.npc.excel;

import com.function.scene.model.SceneObject;
import lombok.Data;

/**
 * @author Catherine
 */
@Data
public class NpcExcel extends SceneObject {
    private Integer id;

    private String name;
    /**
     * 交谈文本
     */
    private String text;

    private Integer status;
}
