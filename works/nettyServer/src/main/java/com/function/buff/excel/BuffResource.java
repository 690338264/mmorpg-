package com.function.buff.excel;

import com.manager.ExcelManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.excel.ClassName;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Catherine
 * @create 2020-08-25 11:34
 */
@Component
public class BuffResource {
    @Autowired
    private ExcelManager excelManager;

    private static Map<Integer, BuffExcel> buffMap = new HashMap<>();

    @PostConstruct
    private void init() {
        int num = excelManager.getMap().get(ClassName.Buff.name()).size();
        for (int i = 0; i < num; i++) {
            BuffExcel buffExcel = (BuffExcel) excelManager.getMap().get(ClassName.Buff.name()).get(i);
            buffMap.put(buffExcel.getId(), buffExcel);
        }

    }

    public static BuffExcel getBuffById(int id) {
        return buffMap.get(id);
    }
}
