package com.function.buff.model;

import com.function.buff.excel.BuffExcel;
import com.function.buff.excel.BuffResource;
import com.function.scene.model.SceneObject;
import lombok.Data;

/**
 * @author Catherine
 * @create 2020-08-25 11:33
 */
@Data

public class Buff {

    public Buff(int id) {
        this.id = id;
    }

    private int id;

    private int remainTimes;

    private SceneObject sceneObject;

    private int atkChange;

    public BuffExcel getBuffExcel() {
        return BuffResource.getBuffById(id);
    }
}
