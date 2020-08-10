package com.function.item.excel;

import com.manager.ExcelManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Catherine
 * @create 2020-08-04 10:49
 */
@Component

public class ItemResource {

    @Autowired
    private ExcelManager excelManager;

    private static Map<Integer, ItemExcel> itemMap = new HashMap<>();

    @PostConstruct
    public void init() {
        int num = excelManager.getMap().get("Item").size();
        for (int i = 0; i < num; i++) {
            ItemExcel itemExcel = (ItemExcel) excelManager.getMap().get("Item").get(i);
            itemMap.put(itemExcel.getId(), itemExcel);
        }
    }

    public static ItemExcel getItemById(int id) {
        return itemMap.get(id);
    }
}
