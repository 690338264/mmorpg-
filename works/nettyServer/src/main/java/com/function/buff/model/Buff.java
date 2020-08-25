package com.function.buff.model;

import com.function.buff.excel.BuffExcel;
import com.function.buff.excel.BuffResource;
import lombok.Data;

/**
 * @author Catherine
 * @create 2020-08-25 11:33
 */
@Data
public class Buff {

    private int id;

    private int remainTime;

    public BuffExcel getBuffExcel() {
        return BuffResource.getBuffById(id);
    }
}
