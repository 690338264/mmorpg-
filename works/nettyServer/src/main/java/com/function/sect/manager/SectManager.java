package com.function.sect.manager;

import com.function.sect.model.Sect;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Catherine
 * @create 2020-09-05 03:08
 */
@Component
public class SectManager {
    private Map<Integer, Sect> sectMap = new ConcurrentHashMap<>();

    public Map<Integer, Sect> getSectMap() {
        return sectMap;
    }
}
