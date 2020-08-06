package com.manager;

import org.springframework.stereotype.Component;
import util.excel.ExcelUtils;

import javax.annotation.PostConstruct;
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

@Component
public class ExcelManager {

    private Map<String, Map<Integer, Object>> map = new HashMap<>();
    private Map<Integer, Object> classMap;


    @PostConstruct
    public void init() throws FileNotFoundException {
        File directory = new File("C:\\Users\\Dell\\Desktop\\mmorpg-\\works\\nettyServer\\src\\main\\resources\\excels\\");

        for (String fileName : directory.list()) {
            classMap = new HashMap<>();
            FileInputStream in = null;
            File file = new File(directory + "\\" + fileName);

            in = new FileInputStream(file);
            String[] s = fileName.split(".xlsx");
            try {
                List<?> list = ExcelUtils.readExcelToEntity(Class.forName("com.function." + s[0].toLowerCase() + ".excel." + s[0] + "Excel"), in, fileName);

                for (int i = 0; i < list.size(); i++) {
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
