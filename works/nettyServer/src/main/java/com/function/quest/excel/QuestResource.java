package com.function.quest.excel;

import com.manager.ExcelManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.excel.ClassName;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Catherine
 * @create 2020-09-11 11:46
 */
@Component
public class QuestResource {
    @Autowired
    private ExcelManager excelManager;

    private static final Map<Integer, QuestExcel> questExcelMap = new HashMap<>();

    @PostConstruct
    private void init() {
        int num = excelManager.getMap().get(ClassName.Quest.name()).size();
        for (int i = 0; i < num; i++) {
            QuestExcel questExcel = (QuestExcel) excelManager.getMap().get(ClassName.Quest.name()).get(i);
            questExcelMap.put(questExcel.getId(), questExcel);
        }
    }

    public static QuestExcel getQuestById(int id) {
        return questExcelMap.get(id);
    }

    public static Map<Integer, QuestExcel> getQuestExcelMap() {
        return questExcelMap;
    }
}
