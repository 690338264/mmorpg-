package com.function.sect.manager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.function.item.model.Item;
import com.function.sect.model.Sect;
import com.jpa.dao.SectDAO;
import com.jpa.manager.JpaManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * @author Catherine
 * @create 2020-09-05 03:08
 */
@Component
@SuppressWarnings("rawtypes")
public class SectManager {
    @Autowired
    private SectDAO sectDAO;
    @Autowired
    private JpaManager jpaManager;
    private final Map<Long, Sect> sectMap = new ConcurrentHashMap<>();

    public Map<Long, Sect> getSectMap() {
        return sectMap;
    }

    @PostConstruct
    private void init() {
        sectDAO.findAll().forEach((tSect) -> {
            Sect sect = new Sect(tSect);
            sect.setMembers(JSON.parseObject(tSect.getMember(), new TypeReference<List<Long>>() {
            }));
            sect.setJoinRequest(JSON.parseObject(tSect.getJoinRequest(), new TypeReference<List<Long>>() {
            }));
            sect.setWareHouse(JSON.parseObject(tSect.getWarehouse(), new TypeReference<Map<Integer, Item>>() {
            }));
            sectMap.put(tSect.getSectId(), sect);
        });
    }

    public void updateSect(Sect sect) {
        ScheduledFuture update = jpaManager.update(sect.getUpdate(), () -> {
            sect.toJson();
            sectDAO.save(sect.gettSect());
            sect.setUpdate(null);
        }, sect.gettSect().getSectId().intValue());
        sect.setUpdate(update);
    }
}
