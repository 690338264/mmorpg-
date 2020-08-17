package com.function.player.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.function.bag.model.Bag;
import com.function.bag.service.BagService;
import com.function.item.excel.ItemExcel;
import com.function.item.model.Item;
import com.function.occ.excel.OccExcel;
import com.function.occ.excel.OccResource;
import com.function.occ.manager.OccCache;
import com.function.player.model.Player;
import com.jpa.dao.BagDAO;
import com.jpa.dao.PlayerDAO;
import com.jpa.entity.TBag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Catherine
 * @create 2020-08-06 19:45
 */
@Component
public class PlayerData {
    @Autowired
    private BagService bagService;
    @Autowired
    private BagDAO bagDAO;
    @Autowired
    private PlayerDAO playerDAO;
    @Autowired
    private OccCache occCache;

    /**
     * 初始化角色属性
     */
    public void initAttribute(Player player) {
        OccExcel occ = OccResource.getOccById(player.getTPlayer().getOccupation());
        int level = player.getTPlayer().getLevel();

        player.setOriHp(occ.getHp() + level * occ.getHp() / occ.getMultiple());
        player.setOriMp(occ.getMp() + level * occ.getMp() / occ.getMultiple());
        player.setHp(player.getOriHp());
        player.setMp(player.getOriMp());

        player.setAtk(occ.getAtk() + level * occ.getAtk() / occ.getMultiple());
        player.setDef(occ.getDef() + level * occ.getDef() / occ.getMultiple());
        player.setSpeed(occ.getSpeed() + level * occ.getSpeed() / occ.getMultiple());
        for (Integer space : player.getEquipMap().keySet()) {
            ItemExcel item = player.getEquipMap().get(space).getItemById();
            int addAtk = item.getAtk();
            int addDef = item.getDef();
            int addSpeed = item.getSpeed();
            player.setAtk(player.getAtk() + addAtk);
            player.setDef(player.getDef() + addDef);
            player.setSpeed(player.getSpeed() + addSpeed);
        }

    }

    /**
     * 初始化角色技能
     */
    public void initSkill(Player player) {
        OccExcel occExcel = occCache.get("Occ" + player.getTPlayer().getOccupation());
        for (int i = 0; i < occExcel.getSkills().size(); i++) {
            player.getSkillMap().put(i + 1, occExcel.getSkills().get(i));
        }
    }

    /**
     * 初始化角色装备
     */
    public void initEquipment(Player player) {
        String json = player.getTPlayer().getEquip();
        Map<Integer, Item> m = JSON.parseObject(json, new TypeReference<Map<Integer, Item>>() {
        });
        player.setEquipMap(m);

    }

    /**
     * 初始化玩家
     */
    public void initPlayer(Player player) {
        initSkill(player);
        initEquipment(player);
        initBag(player);
        initAttribute(player);
    }

    /**
     * 加载背包
     */
    public void initBag(Player player) {
        TBag tBag = bagDAO.findByPlayerId(player.getTPlayer().getRoleId());
        Bag bag = new Bag();
        BeanUtils.copyProperties(tBag, bag);
        player.setBag(bag);
        String json = bag.getItem();
        Map<Integer, Item> m = JSON.parseObject(json, new TypeReference<Map<Integer, Item>>() {
        });
        bag.setItemMap(m);
    }

    /**
     * 更新数据库位置信息
     */
    public void updateLoc(Player player) {
        playerDAO.save(player.getTPlayer());
    }

    /**
     * 更新数据库装备配置
     */
    public void updateEquip(Player player) {
        String json = JSON.toJSONString(player.getEquipMap());
        player.getTPlayer().setEquip(json);
        playerDAO.save(player.getTPlayer());
    }
}
