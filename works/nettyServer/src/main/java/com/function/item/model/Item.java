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
    /**
     * 物品标识
     */
    private Long itemId;
    /**
     * 物品种类的id
     */
    private int id;
    /**
     * 格子里的数量
     */
    private int num;
    /**
     * 实时磨损度
     */
    private int nowWear;

    public Item(int id) {
        this.id = id;
        this.setNowWear(getItemById().getWear());
    }

    public ItemExcel getItemById() {
        return ItemResource.getItemById(id);
    }

}
