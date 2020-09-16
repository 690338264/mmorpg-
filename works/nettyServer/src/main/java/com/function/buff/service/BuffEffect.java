package com.function.buff.service;

import com.function.buff.excel.BuffExcel;
import com.function.scene.model.SceneObject;

import java.util.List;

/**
 * @author Catherine
 * @create 2020-09-15 18:57
 */
public interface BuffEffect {
    /**
     * buff的不同作用效果
     *
     * @param attacker  攻击者
     * @param buffExcel buff
     * @param targets   目标列表
     */
    void buffEffect(SceneObject attacker, List<SceneObject> targets, BuffExcel buffExcel);
}
