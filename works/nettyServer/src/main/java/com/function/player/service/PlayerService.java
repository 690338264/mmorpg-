package com.function.player.service;

import com.event.model.LevelUpEvent;
import com.function.buff.service.BuffService;
import com.function.item.excel.ItemExcel;
import com.function.item.model.Item;
import com.function.item.service.ItemService;
import com.function.monster.model.Monster;
import com.function.monster.service.MonsterService;
import com.function.player.manager.BagManager;
import com.function.player.manager.PlayerManager;
import com.function.player.model.Player;
import com.function.player.model.PlayerInfo;
import com.function.player.model.SceneObjectTask;
import com.function.scene.model.Scene;
import com.function.scene.model.SceneObject;
import com.function.scene.model.SceneObjectType;
import com.function.scene.service.NotifyScene;
import com.function.skill.model.Skill;
import com.function.user.map.UserMap;
import com.jpa.dao.BagDAO;
import com.jpa.dao.PlayerDAO;
import com.jpa.dao.PlayerInfoDAO;
import com.jpa.entity.TBag;
import com.jpa.entity.TPlayer;
import com.jpa.entity.TPlayerInfo;
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
@SuppressWarnings("rawtypes")
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
    private UserMap userMap;
    @Autowired
    private PlayerInfoDAO playerInfoDAO;

    public Long period = 5000L;

    public Long playerRevive = 5000L;

    /**
     * 角色创建
     */
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
            userMap.getUserPlayerMap(userId).put(player.getRoleId(), player);
            TPlayerInfo tplayerInfo = new TPlayerInfo(player.getRoleId(), player.getName(), player.getOccupation());
            PlayerInfo playerInfo = new PlayerInfo(tplayerInfo);
            playerManager.getPlayerInfoMap().put(player.getRoleId(), playerInfo);
            playerInfoDAO.save(tplayerInfo);
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
        if (type == SceneObjectType.PLAYER.getType() && s == player) {
            notifyScene.notifyPlayer(player, "不能攻击自己哦！\n");
            return;
        }
        if (skill == null) {
            notifyScene.notifyPlayer(player, "无效技能\n");
            return;
        }
        //判断技能CD
        if (System.currentTimeMillis() - skill.getLastTime() < skill.getSkillExcel().getCd()) {
            notifyScene.notifyPlayer(player, "技能冷却中\n");
            return;
        }
        //判断玩家mp
        if (player.getMp() < skill.getSkillExcel().getMp()) {
            notifyScene.notifyPlayer(player, "mp不足，技能释放失败\n");
            return;
        }
        //判断装备磨损度
        for (Item equipment : player.getEquipMap().values()) {
            if (equipment.getNowWear() <= 10) {
                notifyScene.notifyPlayer(player, "装备损坏过于严重！请维修\n");
                return;
            }
            equipment.setNowWear(equipment.getNowWear() - 2);
        }
        //造成的伤害
        try {
            s.getLock().lock();
            int hurt = player.getAtk() * skill.getSkillExcel().getAtk();
            s.setHp(s.getHp() - hurt);
            player.setMp(player.getMp() - skill.getSkillExcel().getMp());
            skill.setLastTime(System.currentTimeMillis());
            //击杀
            if (s.getHp() <= 0) {
                //击杀怪物
                if (s.getType() == SceneObjectType.MONSTER) {
                    Monster monster = (Monster) s;
                    killMonster(monster, scene, target, player);
                    return;
                }

                if (s.getType() == SceneObjectType.PLAYER) {
                    Player p = (Player) s;
                    playerDie(p);
                    notifyScene.notifyScene(scene, MessageFormat.format("玩家{0}击败玩家{1}\n",
                            player.getTPlayer().getName(), p.getTPlayer().getName()));
                }
            } else {
                //攻击
                if (s.getType() == SceneObjectType.MONSTER) {
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
                        ScheduledFuture scheduledFuture = ThreadPoolManager.loopThread(() -> {
                            if (monster.getHurtList().isEmpty()) {
                                monster.getTaskMap().get(SceneObjectTask.ATTACK).cancel(true);
                                monster.getTaskMap().remove(SceneObjectTask.ATTACK);
                            }
                            Long hate = monsterService.hurtSort(monster);
                            monsterService.monsterAtk(monster, hate);
                        }, 0, period, monster.getExcelId());
                        monster.getTaskMap().put(SceneObjectTask.ATTACK, scheduledFuture);
                    }
                    return;
                }
                //pvp
                if (s.getType() == SceneObjectType.PLAYER) {
                    Player beAttack = (Player) s;
                    buffService.buff(beAttack.getTPlayer().getRoleId().intValue(), skill, beAttack, player, scene);
                    notifyScene.notifyScene(scene, MessageFormat.format("{0}受到来自{1}的攻击 损失{2}点血\n",
                            beAttack.getTPlayer().getName(), player.getTPlayer().getName(), hurt));
                }
            }
        } finally {
            s.getLock().unlock();
        }
    }


    /**
     * 击杀怪物
     */
    public void killMonster(Monster monster, Scene scene, Long target, Player player) {
        if (!monster.getHurtList().isEmpty()) {
            monster.getTaskMap().get(SceneObjectTask.ATTACK).cancel(true);
            monster.getTaskMap().remove(SceneObjectTask.ATTACK);
        }
        monsterService.monsterDeath(target, scene);
        notifyScene.notifyScene(scene, MessageFormat.format("玩家[{0}]成功击杀怪物{1}\n",
                player.getTPlayer().getName(), monster.getMonsterExcel().getName()));
        //物品掉落
        dropItem(monster, player);
        //金钱经验奖励
        getMoney(player, monster.getMonsterExcel().getMoney());
        getExc(player, monster.getMonsterExcel().getExc());
        notifyScene.notifyPlayer(player, MessageFormat.format("获得{0}经验\n",
                monster.getMonsterExcel().getExc()));
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

//    /**
//     * 获得金币经验
//     */
//    public void getMoneyExc(Monster monster, Player player) {
//        TPlayer tPlayer = player.getTPlayer();
//        int addMoney = monster.getMonsterExcel().getMoney();
//        tPlayer.setMoney(tPlayer.getMoney() + addMoney);
//        int addExc = monster.getMonsterExcel().getExc();
//        tPlayer.setExp(tPlayer.getExp() + addExc);
//        StringBuilder get = new StringBuilder("获得").append(addMoney).append("金钱\n")
//                .append(addExc).append("经验\n");
//        notifyScene.notifyPlayer(player, get);
//        if (tPlayer.getExp() > tPlayer.getLevel() * player.getLevelUp()) {
//            levelUp(player);
//            playerData.initAttribute(player);
//            StringBuilder levelUp = new StringBuilder("恭喜您到达")
//                    .append(tPlayer.getLevel()).append("级\n");
//            notifyScene.notifyPlayer(player, levelUp);
//        }
//    }

    public void getMoney(Player player, int money) {
        TPlayer tPlayer = player.getTPlayer();
        tPlayer.setMoney(tPlayer.getMoney() + money);
        notifyScene.notifyPlayer(player, MessageFormat.format("获得{0}金币\n", money));
    }

    public void getExc(Player player, int exc) {
        TPlayer tPlayer = player.getTPlayer();
        if (tPlayer.getExp() + exc > tPlayer.getLevel() * player.getLevelUp()) {
            levelUp(player, exc);
        }
        tPlayer.setExp(tPlayer.getExp() + exc);
    }

    /**
     * 玩家阵亡
     */
    public boolean playerDie(Player player) {
        if (player.getHp() <= 0) {
            notifyScene.notifyPlayer(player, "已阵亡！五秒后复活！\n");
            ThreadPoolManager.delayThread(() -> player.setHp(player.getOriHp()), playerRevive, player.getChannelHandlerContext().hashCode());
            buffService.removeBuff(player);
            return true;
        }
        return false;
    }

    /**
     * 升级
     */
    public void levelUp(Player player, int exc) {
        TPlayer tPlayer = player.getTPlayer();
        exc = exc - (tPlayer.getLevel() * player.getLevelUp() - tPlayer.getExp());
        tPlayer.setLevel(tPlayer.getLevel() + 1);
        player.submitEvent(new LevelUpEvent());
        notifyScene.notifyPlayer(player, MessageFormat.format("恭喜你升级！您现在的等级为{0}\n", tPlayer.getLevel()));
        tPlayer.setExp(0);
        getExc(player, exc);
    }

}
