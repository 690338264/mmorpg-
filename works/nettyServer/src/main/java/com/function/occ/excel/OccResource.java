package com.function.occ.excel;

import com.manager.ExcelManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Catherine
 */
@Component
public class OccResource {

    @Autowired
    private ExcelManager excelManager;

    private static Map<Integer, OccExcel> occupationMap = new HashMap<>();

    @PostConstruct
    private void init() {
        int num = excelManager.getMap().get("Occ").size();
        for (int i = 0; i < num; i++) {
            OccExcel occExcel = (OccExcel) excelManager.getMap().get("Occ").get(i);
            occupationMap.put(occExcel.getId(), occExcel);
        }
    }

    public static OccExcel getOccById(int id) {
        return occupationMap.get(id);
    }
}
