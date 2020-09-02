package com.function.player.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.function.bag.model.Bag;
import com.function.bag.service.BagService;
import com.function.buff.excel.BuffResource;
import com.function.buff.model.Buff;
import com.function.email.model.Email;
import com.function.item.excel.ItemExcel;
import com.function.item.model.Item;
import com.function.occ.excel.OccExcel;
import com.function.occ.excel.OccResource;
import com.function.occ.manager.OccCache;
import com.function.player.model.Player;
import com.function.scene.model.SceneObjectType;
import com.function.skill.excel.SkillExcel;
import com.function.skill.excel.SkillResource;
import com.function.skill.model.Skill;
import com.jpa.dao.BagDAO;
import com.jpa.dao.EmailDAO;
import com.jpa.dao.PlayerDAO;
import com.jpa.entity.TBag;
import com.jpa.entity.TEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

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
    private EmailDAO emailDAO;
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
        OccExcel occExcel = OccResource.getOccById(player.getTPlayer().getOccupation());
        IntStream.range(0, occExcel.getSkillId().size()).forEach(i -> {
            SkillExcel skillExcel = SkillResource.getSkillById(occExcel.getSkillId().get(i));
            Skill skill = new Skill();
            skill.setSkillId(skillExcel.getId());
            skillExcel.getBuffId().forEach(buffId -> {
                Buff buff = new Buff();
                buff.setId(BuffResource.getBuffById(buffId).getId());
                skill.getBuffList().add(buff);
            });
            player.getCanUseSkill().put(i + 1, skill);
        });
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
     * 初始化邮件
     */
    public void initEmail(Player player) {
        List<TEmail> tEmails = emailDAO.findByPlayerId(player.getTPlayer().getRoleId());
        tEmails.forEach(tEmail -> {
            Email email = new Email();
            email.settEmail(tEmail);
            String json = tEmail.getGift();
            List<Item> gifts = JSON.parseObject(json, new TypeReference<List<Item>>() {
            });
            email.setGifts(gifts);
            player.getEmails().add(email);
        });
    }

    /**
     * 初始化玩家
     */
    public void initPlayer(Player player) {
        player.setType(SceneObjectType.PLAYER.getType());
        initSkill(player);
        initEquipment(player);
        initBag(player);
        initAttribute(player);
        initEmail(player);
    }

    /**
     * 加载背包
     */
    public void initBag(Player player) {
        TBag tBag = bagDAO.findByPlayerId(player.getTPlayer().getRoleId());
        Bag bag = new Bag();
        bag.setTBag(tBag);
        player.setBag(bag);
        String json = tBag.getItem();
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

//    public void updateEmail(Player player) {
//        String json = JSON.toJSONString(player.getEmailMap());
//        player.getTPlayer().setEmail(json);
//        playerDAO.save(player.getTPlayer());
//    }
}
