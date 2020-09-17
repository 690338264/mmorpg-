package com.function.player.service;

import com.event.model.playerEvent.LevelUpEvent;
import com.event.model.playerEvent.MoneyGetEvent;
import com.event.model.playerEvent.MonsterKillEvent;
import com.function.buff.excel.BuffExcel;
import com.function.buff.excel.BuffResource;
import com.function.buff.service.BuffEffectsRealize;
import com.function.item.excel.ItemExcel;
import com.function.item.model.Item;
import com.function.item.service.ItemService;
import com.function.monster.model.Monster;
import com.function.player.manager.BagManager;
import com.function.player.manager.PlayerManager;
import com.function.player.model.Player;
import com.function.player.model.PlayerInfo;
import com.function.player.model.SceneObjectTask;
import com.function.scene.model.Scene;
import com.function.scene.model.SceneObject;
import com.function.scene.model.SceneObjectType;
import com.function.scene.service.NotifyScene;
import com.function.skill.manager.TargetSelector;
import com.function.skill.model.Skill;
import com.function.user.map.UserMap;
import com.jpa.dao.BagDAO;
import com.jpa.dao.PlayerDAO;
import com.jpa.dao.PlayerInfoDAO;
import com.jpa.entity.TBag;
import com.jpa.entity.TPlayer;
import com.jpa.entity.TPlayerInfo;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author Catherine
 */
@Component
public class PlayerService {
    @Autowired
    private PlayerData playerData;
    @Autowired
    private ItemService itemService;
    @Autowired
    private BagManager bagManager;
    @Autowired
    private PlayerManager playerManager;
    @Autowired
    private NotifyScene notifyScene;
    @Autowired
    private PlayerDAO playerDAO;
    @Autowired
    private BagDAO bagDAO;
    @Autowired
    private UserMap userMap;
    @Autowired
    private PlayerInfoDAO playerInfoDAO;
    @Autowired
    private TargetSelector targetSelector;
    @Autowired
    private BuffEffectsRealize buffEffectsRealize;


    /**
     * 角色创建
     */
    public void roleCreate(ChannelHandlerContext ctx, String roleName, Integer roleType, Long userId) {
        for (PlayerInfo playerInfo : playerManager.getPlayerInfoMap().values()) {
            if (playerInfo.gettPlayerInfo().getName().equals(roleName)) {
                ctx.writeAndFlush("角色名已存在！\n");
                return;
            }
        }
        TPlayer tPlayer = playerManager.newPlayer(roleName, roleType, userId);
        synchronized (this) {
            for (PlayerInfo playerInfo : playerManager.getPlayerInfoMap().values()) {
                if (playerInfo.gettPlayerInfo().getName().equals(roleName)) {
                    ctx.writeAndFlush("角色名已存在！\n");
                    return;
                }
            }
            playerDAO.saveAndFlush(tPlayer);
        }
        TBag bag = bagManager.newBag(tPlayer);
        bagDAO.save(bag);
        TPlayerInfo tplayerInfo = new TPlayerInfo(tPlayer.getUserId(), tPlayer.getRoleId(), tPlayer.getName(), tPlayer.getOccupation());
        userMap.getUserPlayerMap(userId).put(tPlayer.getRoleId(), tplayerInfo);
        PlayerInfo playerInfo = new PlayerInfo(tplayerInfo);
        playerManager.getPlayerInfoMap().put(tPlayer.getRoleId(), playerInfo);
        playerInfoDAO.save(tplayerInfo);
        ctx.writeAndFlush("角色创建成功，角色id:" + tPlayer.getRoleId() + "角色昵称为：" + tPlayer.getName() + '\n');
    }


    /**
     * 显示人物状态
     */
    public void showState(Player player) {
        TPlayer tPlayer = player.getTPlayer();
        StringBuilder state = new StringBuilder("您当前hp:[").append(player.getHp()).append("]\n")
                .append("您当前mp:[").append(player.getMp()).append("]\n")
                .append("您当前等级为:[").append(tPlayer.getLevel()).append("]\n")
                .append("经验值").append(tPlayer.getExp()).append("/").append(tPlayer.getLevel() * player.getLevelUp()).append('\n')
                .append("您当前攻击力为:[").append(player.getAtk()).append("]\n")
                .append("您当前防御力为:[").append(player.getDef()).append("]\n");
        notifyScene.notifyPlayer(player, state);
    }

    /**
     * 释放技能
     */
    public void useSkill(SceneObject attacker, int skillId, long target, SceneObjectType type) {
        if (!targetSelector.checkIfCan(attacker, skillId)) {
            return;
        }
        //获得目标
        Map<Integer, List<SceneObject>> targets = targetSelector.chooseTarget(attacker, skillId, target, type);
        for (List<SceneObject> value : targets.values()) {
            if (value.isEmpty()) {
                notifyScene.notifyPlayer(attacker, "目标无效!\n");
                return;
            }
        }
        if (attacker.getType() == SceneObjectType.PLAYER) {
            Skill skill = attacker.getCanUseSkill().get(skillId);
            attacker.setMp(attacker.getMp() - skill.getSkillExcel().getMp());
            skill.setLastTime(System.currentTimeMillis());

        }
        targets.forEach((buffId, targetList) -> {
            BuffExcel buffExcel = BuffResource.getBuffById(buffId);
            buffEffectsRealize.effect(attacker, targetList, buffExcel);
        });

    }

    /**
     * 击杀怪物
     */
    public void killMonster(Monster monster, Scene scene, Player player) {
        if (!monster.getHurtList().isEmpty()) {
            monster.getTaskMap().get(SceneObjectTask.ATTACK).cancel(true);
            monster.getTaskMap().remove(SceneObjectTask.ATTACK);
        }
        notifyScene.notifyScene(scene, MessageFormat.format("玩家[{0}]成功击杀怪物{1}\n",
                player.getTPlayer().getName(), monster.getMonsterExcel().getName()));
        //物品掉落
        dropItem(monster, player);
        //金钱经验奖励
        getMoney(player, monster.getMonsterExcel().getMoney());
        notifyScene.notifyPlayer(player, MessageFormat.format("获得{0}经验\n",
                monster.getMonsterExcel().getExc()));
        getExc(player, monster.getMonsterExcel().getExc());
        player.asynchronousSubmitEvent(new MonsterKillEvent(monster.getExcelId()));
        playerData.updatePlayer(player);
    }

    /**
     * 物品掉落
     */
    public void dropItem(Monster monster, Player player) {
        Random random = new Random();
        List<ItemExcel> list = monster.getMonsterExcel().getItemList();
        int index = random.nextInt(list.size());
        Item item = new Item(list.get(index).getId());
        item.setNum(1);
        itemService.getItem(item, player);
    }

    /**
     * 获得金币
     */
    public void getMoney(Player player, int money) {
        TPlayer tPlayer = player.getTPlayer();
        tPlayer.setMoney(tPlayer.getMoney() + money);
        notifyScene.notifyPlayer(player, MessageFormat.format("获得{0}金币\n", money));
        player.asynchronousSubmitEvent(new MoneyGetEvent(money));
    }

    /**
     * 获得经验
     */
    public void getExc(Player player, int exc) {
        TPlayer tPlayer = player.getTPlayer();
        if (tPlayer.getExp() + exc > tPlayer.getLevel() * player.getLevelUp()) {
            levelUp(player, exc);
        } else {
            tPlayer.setExp(tPlayer.getExp() + exc);
        }
    }

    /**
     * 升级
     */
    public void levelUp(Player player, int exc) {
        TPlayer tPlayer = player.getTPlayer();
        exc = exc - (tPlayer.getLevel() * player.getLevelUp() - tPlayer.getExp());
        tPlayer.setLevel(tPlayer.getLevel() + 1);
        player.asynchronousSubmitEvent(new LevelUpEvent());
        notifyScene.notifyPlayer(player, MessageFormat.format("恭喜你升级！您现在的等级为{0}\n", tPlayer.getLevel()));
        tPlayer.setExp(0);
        getExc(player, exc);
    }

}
