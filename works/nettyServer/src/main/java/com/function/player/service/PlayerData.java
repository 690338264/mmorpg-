package com.function.player.service;

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
import com.function.player.manager.BagInit;
import com.function.player.manager.InitManager;
import com.function.player.manager.PlayerInit;
import com.function.player.model.PlayerModel;
import com.function.skill.model.Skill;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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
        playerModel.setOriHp(occ.getHp() + playerModel.getLevel() * occ.getHp() / 5 * 4);
        playerModel.setOriMp(occ.getMp() + playerModel.getLevel() * occ.getMp() / 5 * 4);

        playerModel.setAtk(occ.getAtk() + playerModel.getLevel() * occ.getAtk() / 6 * 5);
        playerModel.setDef(occ.getDef() + playerModel.getLevel() * occ.getDef() / 6 * 5);
        playerModel.setSpeed(occ.getSpeed() + playerModel.getLevel() * occ.getSpeed() / 6 * 5);
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
        String[] equips = playerModel.getEquip().split(",");
        for (int i = 0; i < equips.length && !"".equals(playerModel.getEquip()) && playerModel.getEquip() != null; i++) {
            int itemId = Integer.parseInt(equips[i]);
            Item item = new Item();
            item.setId(itemId);
            item.setNowWear(item.getItemById().getWear());
            playerModel.getEquipMap().put(item.getItemById().getSpace(), item);
        }
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

        setInitManager(new BagInit());
        BagExample bagExample = (BagExample) initData(playerModel);
        List<Bag> bagList = bagMapper.selectByExample(bagExample);
        BagModel bagModel = new BagModel();
        BeanUtils.copyProperties(bagList.get(0), bagModel);
        playerModel.setBagModel(bagModel);

        if (bagModel.getItem().equals("") && bagModel.getItem() != null) {
            return;
        }

        String[] items = bagModel.getItem().split(",");
        for (int i = 0; i < items.length; i++) {
            int itemId = Integer.parseInt(items[i]);
            Item item = new Item();
            item.setId(itemId);
            item.setNum(1);
            item.setNowWear(item.getItemById().getWear());
            bagModel.getItemMap().put(i, item);
        }

        bagService.orderBag(playerModel, playerModel.getBagModel().getItemMap());
    }

    /**
     * 更新数据库位置信息
     */
    public void updateLoc(Integer loc, PlayerModel playerModel) {
        setInitManager(new PlayerInit());
        PlayerExample playerExample = (PlayerExample) initData(playerModel);
        Player newPlayer = new Player();
        newPlayer.setLoc(loc);
        playerMapper.updateByExampleSelective(newPlayer, playerExample);
    }

    /**
     * 更新数据库装备配置
     */
    public void updateEquip(PlayerModel playerModel) {
        setInitManager(new PlayerInit());
        PlayerExample playerExample = (PlayerExample) initData(playerModel);
        Player newPlayer = new Player();
        newPlayer.setEquip(playerModel.getEquip());
        playerMapper.updateByExampleSelective(newPlayer, playerExample);
    }

    public void setInitManager(InitManager initManager) {
        this.initManager = initManager;
    }

    public Object initData(PlayerModel playerModel) {
        return initManager.init(playerModel);
    }
}
