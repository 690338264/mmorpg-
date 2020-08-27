package com.function.buff.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.function.buff.excel.BuffExcel;
import com.function.buff.excel.BuffResource;
import com.function.scene.model.SceneObject;
import lombok.Data;

/**
 * @author Catherine
 * @create 2020-08-25 11:33
 */
@Data
@JsonIgnoreProperties(value = {"sceneObject"}, ignoreUnknown = true)

public class Buff {

    private int id;

    private int remainTimes;

    private SceneObject sceneObject;

    public BuffExcel getBuffExcel() {
        return BuffResource.getBuffById(id);
    }
}
