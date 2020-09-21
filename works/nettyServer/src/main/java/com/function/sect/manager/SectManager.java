package com.function.sect.manager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.function.item.model.Item;
import com.function.sect.model.Sect;
import com.function.sect.service.SectService;
import com.jpa.dao.SectDAO;
import com.jpa.entity.TSect;
import com.manager.UpdateThreadManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Catherine
 * @create 2020-09-05 03:08
 */
@Component
public class SectManager {
    @Autowired
    private SectDAO sectDAO;
    private final Map<Long, Sect> sectMap = new ConcurrentHashMap<>();

    private final Set<String> sectNameSet = new HashSet<>();

    public Map<Long, Sect> getSectMap() {
        return sectMap;
    }

    public Set<String> getSectNameSet() {
        return sectNameSet;
    }

    @PostConstruct
    private void init() {
        List<TSect> tSects = sectDAO.findAll();
        tSects.forEach((tSect) -> {
            Sect sect = new Sect(tSect);
            sect.setMembers(JSON.parseObject(tSect.getMember(), new TypeReference<List<Long>>() {
            }));
            sect.setJoinRequest(JSON.parseObject(tSect.getJoinRequest(), new TypeReference<List<Long>>() {
            }));
            sect.setWareHouse(JSON.parseObject(tSect.getWarehouse(), new TypeReference<Map<Integer, Item>>() {
            }));
            sectMap.put(tSect.getSectId(), sect);
            sectNameSet.add(tSect.getName());
        });
        if (tSects.size() == 0) {
            return;
        }
        SectService.incSectId.getAndAdd(tSects.get(tSects.size() - 1).getSectId());
    }

    public void updateSect(Sect sect) {
        sect.toJson();
        UpdateThreadManager.putIntoThreadPool(sect.getClass(), sect.gettSect().getSectId(), () -> {
            sectDAO.save(sect.gettSect());
        });
    }
}
