package com.function.item.model;

import com.function.item.excel.ItemExcel;
import com.function.item.excel.ItemResource;
import lombok.Data;

/**
 * @author Catherine
 * @create 2020-08-04 10:55
 */
@Data
public class Item {
    private Integer id;

    private int num;

    private int nowWear;

    public ItemExcel getItemById() {
        return ItemResource.getItemById(id);
    }
}
