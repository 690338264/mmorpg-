package com.function.player.service;

import com.function.buff.service.BuffService;
import com.function.item.excel.ItemExcel;
import com.function.item.model.Item;
import com.function.item.service.ItemService;
import com.function.monster.model.Monster;
import com.function.monster.service.MonsterService;
import com.function.player.manager.BagManager;
import com.function.player.manager.PlayerManager;
import com.function.player.model.Player;
import com.function.player.model.SceneObjectTask;
import com.function.scene.model.Scene;
import com.function.scene.model.SceneObject;
import com.function.scene.model.SceneObjectType;
import com.function.scene.service.NotifyScene;
import com.function.skill.model.Skill;
import com.function.user.map.UserMap;
import com.jpa.dao.BagDAO;
import com.jpa.dao.EmailDAO;
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
import java.util.concurrent.ScheduledFuture;

/**
 * @author Catherine
 */
@Component
public class PlayerService {

    @Autowired
    private MonsterService monsterService;
    @Autowired
    private BuffService buffService;
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
    private EmailDAO emailDAO;
    @Autowired
    private UserMap userMap;

    public Long period = 5000L;

    public Long playerRevive = 5000L;

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
            Player p = new Player();
            p.setTPlayer(player);
            p.setInit(false);
            userMap.getPlayerMap(userId).put(player.getRoleId(), p);
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
     * 攻击
     */
    public void attack(Player player, int skillId, Long target, int type) {

        Scene scene = player.getNowScene();
        SceneObject s = scene.getSceneObjectMap().get(type).get(target);
        Skill skill = player.getCanUseSkill().get(skillId);
        //判断目标是否死亡
        if (s == null) {
            player.getChannelHandlerContext().writeAndFlush("攻击目标无效，请重新选择！\n");
            return;
        }
        //判断技能CD
        if (skill != null) {
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
                //造成的伤害
                try {
                    s.getLock().lock();
                    int hurt = player.getAtk() * skill.getSkillExcel().getAtk();
                    s.setHp(s.getHp() - hurt);
                    player.setMp(player.getMp() - skill.getSkillExcel().getMp());
                    player.getCanUseSkill().remove(skillId);
                    ThreadPoolManager.delayThread(() -> player.getCanUseSkill().put(skillId, skill), skill.getSkillExcel().getCd(), player.getChannelHandlerContext().hashCode());
                    //击杀
                    if (s.getHp() <= 0) {
                        //击杀怪物
                        if (s.getType() == SceneObjectType.MONSTER.getType()) {
                            Monster monster = (Monster) s;
                            killMonster(monster, scene, target, player);
                            return;
                        }

                        if (s.getType() == SceneObjectType.PLAYER.getType()) {
                            Player p = (Player) s;
                            notifyScene.notifyScene(scene, MessageFormat.format("玩家{0}击败玩家{1}\n",
                                    player.getTPlayer().getName(), p.getTPlayer().getName()));
                            return;
                        }
                    } else {
                        //攻击
                        if (s.getType() == SceneObjectType.MONSTER.getType()) {
                            Monster monster = (Monster) s;
                            int oriHurt;
                            int flag = 0;
                            if (monster.getHurtList().isEmpty()) {
                                flag = 1;
                            }
                            oriHurt = monster.getHurtList().getOrDefault(player.getTPlayer().getRoleId(), 0);
                            monster.getHurtList().put(player.getTPlayer().getRoleId(), hurt + oriHurt);
                            buffService.buff(monster.getId().intValue(), skill, monster, player, scene);
                            notifyScene.notifyScene(scene, MessageFormat.format("玩家[{0}]释放了技能[{1}]对怪物[{2}]产生伤害:{3}\n",
                                    player.getTPlayer().getName(), skill.getSkillExcel().getName(),
                                    monster.getMonsterExcel().getName(), hurt));
                            if (flag == 1) {
                                int attack = SceneObjectTask.ATTACK.getKey();
                                ScheduledFuture scheduledFuture = ThreadPoolManager.loopThread(() -> {
                                    if (monster.getHurtList().isEmpty()) {
                                        monster.getTaskMap().get(attack).cancel(true);
                                        monster.getTaskMap().remove(attack);
                                    }
                                    Long hate = monsterService.hurtSort(monster);
                                    monsterService.monsterAtk(monster, hate);
                                }, 0, period, monster.getExcelId());
                                monster.getTaskMap().put(attack, scheduledFuture);
                            }
                        }
                    }
                } finally {
                    s.getLock().unlock();
                }
            } else {
                notifyScene.notifyPlayer(player, "技能释放失败！原因：mp不足！\n");
            }
        } else {
            notifyScene.notifyPlayer(player, "技能冷却中\n");
        }


    }


    /**
     * 击杀怪物
     */
    public void killMonster(Monster monster, Scene scene, Long target, Player player) {
        int attack = SceneObjectTask.ATTACK.getKey();
        if (!monster.getHurtList().isEmpty()) {
            monster.getTaskMap().get(attack).cancel(true);
            monster.getTaskMap().remove(attack);
        }
        monsterService.monsterDeath(target, scene);
        notifyScene.notifyScene(scene, MessageFormat.format("玩家[{0}]成功击杀怪物{1}\n",
                player.getTPlayer().getName(), monster.getMonsterExcel().getName()));
        //物品掉落
        dropItem(monster, player);
        //金钱经验奖励
        getMoneyExc(monster, player);
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
     * 获得金币经验
     */
    public void getMoneyExc(Monster monster, Player player) {
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
    }

    /**
     * 玩家阵亡
     */
    public boolean playerDie(Player player, Monster monster) {
        if (player.getHp() <= 0) {
            notifyScene.notifyPlayer(player, "已阵亡！五秒后复活！\n");
            ThreadPoolManager.delayThread(() -> player.setHp(player.getOriHp()), playerRevive, player.getChannelHandlerContext().hashCode());
            buffService.removeBuff(player);
            monster.getHurtList().remove(player.getTPlayer().getRoleId());
            return true;
        }
        return false;
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
