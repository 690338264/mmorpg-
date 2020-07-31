package com.manager;

import util.excel.ExcelUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Catherine
 * @create 2020-07-31 12:16
 */
public class ExcelManager {
    private Map<String, Map<Integer, Object>> map = new HashMap<>();
    private Map<Integer, Object> classMap = new HashMap<>();

    public void init() throws FileNotFoundException {
        File directory = new File("D:\\works\\zhi\\src\\main\\resources\\");

        for (String fileName : directory.list()) {
            FileInputStream in = null;
            File file = new File(directory + "\\" + fileName + "Excel");

            in = new FileInputStream(file);
            String[] s = fileName.split(".xlsx");
            try {
                List<?> list = ExcelUtils.readExcelToEntity(Class.forName(s[0]), in, fileName);

                for (int i = 0; i < list.size(); i++) {
                    list.get(i);
                    classMap.put(i + 1, list.get(i));
                }
                map.put(s[0], classMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Map<String, Map<Integer, Object>> getMap() {
        return map;
    }
}
