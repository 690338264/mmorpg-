package com.function.player.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.database.entity.Bag;
import com.database.entity.BagExample;
import com.database.entity.Player;
import com.database.entity.PlayerExample;
import com.database.mapper.BagMapper;
import com.database.mapper.PlayerMapper;
import com.function.bag.model.BagModel;
import com.function.bag.service.BagService;
import com.function.item.model.Item;
import com.function.occ.excel.OccExcel;
import com.function.occ.excel.OccResource;
import com.function.player.manager.BagManager;
import com.function.player.manager.InitManager;
import com.function.player.manager.PlayerManager;
import com.function.player.model.PlayerModel;
import com.function.skill.model.Skill;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author Catherine
 * @create 2020-08-06 19:45
 */
@Component
public class PlayerData {
    @Autowired
    private BagMapper bagMapper;
    @Autowired
    private BagService bagService;
    @Autowired
    private PlayerMapper playerMapper;
    @Autowired
    private PlayerManager playerManager;

    InitManager initManager;

    public void initLevel(PlayerModel playerModel) {
        playerModel.setLevel(playerModel.getExp() / 2000);
    }

    /**
     * 初始化角色属性
     */
    public void initAttribute(PlayerModel playerModel) {
        OccExcel occ = OccResource.getOccById(playerModel.getOccupation());

        playerModel.setHp(occ.getHp());
        playerModel.setMp(occ.getMp());
        playerModel.setOriHp(occ.getHp() + playerModel.getLevel() * occ.getHp() / occ.getMultiple());
        playerModel.setOriMp(occ.getMp() + playerModel.getLevel() * occ.getMp() / occ.getMultiple());

        playerModel.setAtk(occ.getAtk() + playerModel.getLevel() * occ.getAtk() / occ.getMultiple());
        playerModel.setDef(occ.getDef() + playerModel.getLevel() * occ.getDef() / occ.getMultiple());
        playerModel.setSpeed(occ.getSpeed() + playerModel.getLevel() * occ.getSpeed() / occ.getMultiple());
        for (Integer space : playerModel.getEquipMap().keySet()) {
            int addAtk = playerModel.getEquipMap().get(space).getItemById().getAtk();
            int addDef = playerModel.getEquipMap().get(space).getItemById().getDef();
            int addSpeed = playerModel.getEquipMap().get(space).getItemById().getSpeed();
            playerModel.setAtk(playerModel.getAtk() + addAtk);
            playerModel.setDef(playerModel.getDef() + addDef);
            playerModel.setSpeed(playerModel.getSpeed() + addSpeed);
        }

    }

    /**
     * 初始化角色技能
     */
    public void initSkill(PlayerModel playerModel) {
        String skills = OccResource.getOccById(playerModel.getOccupation()).getSkill();
        String[] str = skills.split(",");
        for (int i = 0; i < str.length; i++) {
            int type = Integer.parseInt(str[i]);
            Skill skill = new Skill();
            skill.setSkillId(type);
            playerModel.getSkillMap().put(i + 1, skill);
        }
    }

    /**
     * 初始化角色装备
     */
    public void initEquipment(PlayerModel playerModel) {
        String json = playerModel.getEquip();
        Map<Integer, Item> m = JSON.parseObject(json, new TypeReference<Map<Integer, Item>>() {
        });
        playerModel.setEquipMap(m);

    }

    /**
     * 初始化玩家
     */
    public void initPlayer(PlayerModel playerModel) {
        initLevel(playerModel);
        initSkill(playerModel);
        initEquipment(playerModel);
        initBag(playerModel);
        initAttribute(playerModel);
    }

    /**
     * 加载背包
     */
    public void initBag(PlayerModel playerModel) {

        setInitManager(new BagManager());
        BagExample bagExample = (BagExample) initData(playerModel);
        List<Bag> bagList = bagMapper.selectByExample(bagExample);
        BagModel bagModel = new BagModel();
        BeanUtils.copyProperties(bagList.get(0), bagModel);
        playerModel.setBagModel(bagModel);
        String json = bagModel.getItem();
        Map<Integer, Item> m = JSON.parseObject(json, new TypeReference<Map<Integer, Item>>() {
        });
        bagModel.setItemMap(m);

        bagService.orderBag(playerModel, playerModel.getBagModel().getItemMap());
    }

    /**
     * 更新数据库位置信息
     */
    public void updateLoc(Integer loc, PlayerModel playerModel) {
        //
        setInitManager(playerManager);
        PlayerExample playerExample = (PlayerExample) initData(playerModel);
        Player newPlayer = new Player();
        newPlayer.setLoc(loc);
        playerMapper.updateByExampleSelective(newPlayer, playerExample);
    }

    /**
     * 更新数据库装备配置
     */
    public void updateEquip(PlayerModel playerModel) {
        setInitManager(playerManager);
        PlayerExample playerExample = (PlayerExample) initData(playerModel);
        Player newPlayer = new Player();
        String json = JSON.toJSONString(playerModel.getEquipMap());
        newPlayer.setEquip(json);
        playerMapper.updateByExampleSelective(newPlayer, playerExample);
    }

    public void setInitManager(InitManager initManager) {
        this.initManager = initManager;
    }

    public Object initData(PlayerModel playerModel) {
        return initManager.init(playerModel);
    }
}
