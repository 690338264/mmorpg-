package com.function.buff.map;

import com.function.buff.model.Buff;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Catherine
 * @create 2020-08-25 16:58
 */
@Component
public class BuffMap {

    private Map<Integer, Buff> buffCache = new HashMap<>();

    public Map<Integer, Buff> getBuffCache() {
        return buffCache;
    }

    public Buff get(Integer k) {
        return buffCache.get(k);
    }

    public void put(Integer k, Buff v) {
        buffCache.put(k, v);
    }
}
