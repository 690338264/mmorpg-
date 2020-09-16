package com.function.npc.excel;

import com.function.scene.model.SceneObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * @author Catherine
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NpcExcel extends SceneObject {
    private int id;

    private String name;
    /**
     * 交谈文本
     */
    private String text;

    private Integer status;

    @Override
    public Long getId() {
        return (long) id;
    }

    @Override
    public Map<String, Object> getAttributeMap() {
        return null;
    }
}
