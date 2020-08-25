package com.function.player.service;

import com.function.buff.model.Buff;
import com.function.item.excel.ItemExcel;
import com.function.item.model.Item;
import com.function.item.service.ItemService;
import com.function.monster.model.Monster;
import com.function.monster.service.MonsterService;
import com.function.monster.timetask.AtkTime;
import com.function.player.manager.BagManager;
import com.function.player.manager.PlayerManager;
import com.function.player.model.Player;
import com.function.scene.model.Scene;
import com.function.scene.service.NotifyScene;
import com.function.skill.model.Skill;
import com.jpa.dao.BagDAO;
import com.jpa.dao.PlayerDAO;
import com.jpa.entity.TBag;
import com.jpa.entity.TPlayer;
import com.manager.ThreadPoolManager;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;
import java.util.Random;

/**
 * @author Catherine
 */
@Component
public class PlayerService {

    @Autowired
    private MonsterService monsterService;
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

    public void roleCreate(ChannelHandlerContext ctx, String roleName, Integer roleType, Long userId) {
        TPlayer tPlayer = playerManager.newPlayer(roleName, roleType, userId);
        if (playerDAO.findByName(roleName) != null) {
            ctx.writeAndFlush("角色名已存在！\n");
            return;
        }
        playerDAO.save(tPlayer);
        TPlayer player = playerDAO.findByName(roleName);
        if (player != null) {
            TBag bag = bagManager.newBag(player);
            bagDAO.save(bag);
            ctx.writeAndFlush("角色创建成功，角色id:" + player.getRoleId() + "角色昵称为：" + player.getName() + '\n');
        } else {
            ctx.writeAndFlush("角色创建失败！\n");
        }

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
     * 攻击怪物
     */
    public void attackMonster(Player player, int skillId, int target) {
        synchronized (this) {
            Scene scene = player.getNowScene();
            Monster monster = scene.getMonsterMap().get(target);
            Skill skill = player.getSkillMap().get(skillId);
            Long now = System.currentTimeMillis();
            //判断目标是否死亡
            if (monster == null) {
                player.getChannelHandlerContext().writeAndFlush("攻击目标无效，请重新选择！\n");
                return;
            }
            //判断技能CD
            if (skill.getLastTime() == null || now - skill.getLastTime() >= skill.getSkillExcel().getCd()) {
                //判断玩家mp
                if (player.getMp() >= skill.getSkillExcel().getMp()) {
                    //判断装备磨损度
                    for (Integer key : player.getEquipMap().keySet()) {
                        if (player.getEquipMap().get(key).getNowWear() <= 10) {
                            StringBuilder eqpBreak = new StringBuilder("装备损坏过于严重！请维修\n");
                            notifyScene.notifyPlayer(player, eqpBreak);
                            return;
                        }
                        player.getEquipMap().get(key).setNowWear(player.getEquipMap().get(key).getNowWear() - 2);
                    }
                    //玩家对怪物造成的伤害
                    int hurt = player.getAtk() * skill.getSkillExcel().getAtk();
                    monster.setHp(monster.getHp() - hurt);
                    player.setMp(player.getMp() - skill.getSkillExcel().getMp());
                    skill.setLastTime(System.currentTimeMillis());

                    //击杀怪物
                    if (monsterService.isMonsterDeath(target, scene)) {
                        if (monster.getTarget() != null) {
                            monster.getAtkTime().cancel();
                        }
                        notifyScene.notifyScene(scene, MessageFormat.format("玩家[{0}]成功击杀怪物{1}\n",
                                player.getTPlayer().getName(), monster.getMonsterExcel().getName()));
                        //物品掉落
                        Random random = new Random();
                        List<ItemExcel> list = monster.getMonsterExcel().getItemList();
                        int index = random.nextInt(list.size());
                        Item item = new Item();
                        item.setId(list.get(index).getId());
                        item.setNowWear(item.getItemById().getWear());
                        itemService.getItem(item, player);
                        //金钱经验奖励
                        TPlayer tPlayer = player.getTPlayer();
                        int addMoney = monster.getMonsterExcel().getMoney();
                        tPlayer.setMoney(tPlayer.getMoney() + addMoney);
                        int addExc = monster.getMonsterExcel().getExc();
                        tPlayer.setExp(tPlayer.getExp() + addExc);
                        StringBuilder get = new StringBuilder("获得").append(addMoney).append("金钱\n")
                                .append(addExc).append("经验\n");
                        notifyScene.notifyPlayer(player, get);
                        if (tPlayer.getExp() > tPlayer.getLevel() * player.getLevelUp()) {
                            levelUp(player);
                            playerData.initAttribute(player);
                            StringBuilder levelUp = new StringBuilder("恭喜您到达")
                                    .append(tPlayer.getLevel()).append("级\n");
                            notifyScene.notifyPlayer(player, levelUp);
                        }

                    } else {
                        //攻击
                        notifyScene.notifyScene(scene, MessageFormat.format("玩家[{0}]释放了技能[{1}]对怪物[{2}]产生伤害:{3}\n",
                                player.getTPlayer().getName(), skill.getSkillExcel().getName(), monster.getMonsterExcel().getName(), hurt));

                        if (monster.getTarget() == null) {
                            monster.setTarget(player);
                            monster.setAtkTime(new AtkTime(player, monster, scene));
                            monster.getTimer().schedule(monster.getAtkTime(), 0, 5000);
                        }
                    }

                } else {
                    StringBuilder noMp = new StringBuilder("技能释放失败！原因：mp不足！\n");
                    notifyScene.notifyPlayer(player, noMp);
                }

            } else {
                StringBuilder waitCd = new StringBuilder("技能[").append(skill.getSkillExcel().getName()).append("]冷却中\n");
                notifyScene.notifyPlayer(player, waitCd);
            }
        }
    }

    /**
     * buff效果
     */
    public void buff(int id, Skill skill, int type) {
        skill.getBuffMap().forEach((k, v) -> {
            Buff buff = skill.getBuffMap().get(k);
            long time = buff.getBuffExcel().getLast() / buff.getBuffExcel().getTimes();
            //多次效果
            if (buff.getBuffExcel().getTimes() != 1) {
                ThreadPoolManager.loopThread(() -> {

                }, 0, time, id);
            }
        });
    }

    /**
     * 升级
     */
    public void levelUp(Player player) {
        TPlayer tplayer = player.getTPlayer();
        tplayer.setExp(tplayer.getExp() - tplayer.getLevel() * player.getLevelUp());
        tplayer.setLevel(tplayer.getLevel() + 1);
    }

}
