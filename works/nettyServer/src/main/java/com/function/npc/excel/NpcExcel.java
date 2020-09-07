package com.function.npc.excel;

import com.function.scene.model.SceneObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Catherine
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NpcExcel extends SceneObject {
    private Integer id;

    private String name;
    /**
     * 交谈文本
     */
    private String text;

    private Integer status;
}
