package com.function.player.service;

import com.function.item.excel.ItemExcel;
import com.function.item.model.Item;
import com.function.item.service.ItemService;
import com.function.monster.model.Monster;
import com.function.monster.service.MonsterService;
import com.function.monster.timetask.AtkTime;
import com.function.player.controller.Time;
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
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.Timer;

/**
 * @author Catherine
 */
@Component
public class PlayerService {

    @Autowired
    private MonsterService monsterService;
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
    public void showState(ChannelHandlerContext ctx, Player player) {
        ctx.writeAndFlush("您当前hp:[" + player.getHp() + "]\n" +
                "您当前mp:[" + player.getMp() + "]\n" +
                "您当前等级为:[" + player.getTPlayer().getLevel() + "]\n" +
                "您当前攻击力为:[" + player.getAtk() + "]\n" +
                "您当前防御力为:[" + player.getDef() + "]\n");
    }

    /**
     * 攻击怪物
     */
    public void attackMonster(Player player, int skillId, int target) {
        synchronized (this) {
            Scene scene = player.getNowScene();
            Monster monster = scene.getSceneExcel().getMonsters().get(target);
            Skill skill = player.getSkillMap().get(skillId);
            Long now = System.currentTimeMillis();
            //判断目标是否死亡
            if (monster == null) {
                player.getChannelHandlerContext().writeAndFlush("攻击目标无效，请重新选择！\n");
                return;
            }
            //判断技能CD
            if (skill.getLastTime() == null || now - skill.getLastTime() >= skill.getSkillExcel().getCd() * 1000) {
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
                    int hurt = player.getAtk() * skill.getSkillExcel().getBuff();
                    monster.setSelfHp(monster.getSelfHp() - hurt);
                    player.setMp(player.getMp() - skill.getSkillExcel().getMp());
                    skill.setLastTime(System.currentTimeMillis());

                    //击杀怪物
                    if (monsterService.isMonsterDeath(target, scene)) {
                        if (monster.getTarget() != null) {
                            monster.getAtkTime().cancel();
                        }
                        StringBuilder notify = new StringBuilder();
                        notify.append("玩家[").append(player.getTPlayer().getName()).append("]成功击杀怪物").append(monster.getMonsterExcel().getName()).append('\n');
                        notifyScene.notifyScene(scene, notify);
                        Random random = new Random();
                        List<ItemExcel> list = monster.getMonsterExcel().getItemList();
                        int index = random.nextInt(list.size());
                        Item item = new Item();
                        item.setId(list.get(index).getId());
                        item.setNowWear(item.getItemById().getWear());
                        itemService.getItem(item, player);

                    } else {
                        //攻击
                        StringBuilder notify = new StringBuilder();
                        notify.append("玩家[").append(player.getTPlayer().getName()).append("]释放了技能[").append(skill.getSkillExcel().getName()).append("]对怪物[")
                                .append(monster.getMonsterExcel().getName()).append("]产生伤害:").append(hurt).append('\n');
                        notifyScene.notifyScene(scene, notify);

                        if (monster.getTarget() == null) {
                            monster.setTarget(player);
                            monster.setAtkTime(new AtkTime(player, monster, scene));
                            monster.getTimer().schedule(monster.getAtkTime(), 0, 5000);
                        }
                    }
//              mp恢复
                    if (player.getMpTimer() == null) {
                        player.setMpTimer(new Timer());
                        Time t = new Time();
                        t.setPlayer(player);
                        player.getMpTimer().schedule(t, 0, 3000);
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

}
