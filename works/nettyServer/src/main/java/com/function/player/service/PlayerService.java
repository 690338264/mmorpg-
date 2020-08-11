package com.function.player.service;

import com.database.entity.Bag;
import com.database.entity.Player;
import com.database.entity.PlayerExample;
import com.database.mapper.BagMapper;
import com.database.mapper.PlayerMapper;
import com.function.item.model.Item;
import com.function.item.service.ItemService;
import com.function.monster.model.Monster;
import com.function.monster.service.MonsterService;
import com.function.player.controller.Time;
import com.function.player.manager.BagManager;
import com.function.player.manager.PlayerManager;
import com.function.player.model.PlayerModel;
import com.function.scene.model.Scene;
import com.function.scene.service.NotifyScene;
import com.function.skill.model.Skill;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Timer;

/**
 * @author Catherine
 */
@Component
public class PlayerService {
    @Autowired
    private PlayerMapper playerMapper;
    @Autowired
    private BagMapper bagMapper;
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

    public void roleCreate(ChannelHandlerContext ctx, String roleName, Integer roleType, Long userId) {
        Player player = playerManager.newPlayer(roleName, roleType, userId);
        try {
            playerMapper.insertSelective(player);
        } catch (DuplicateKeyException e) {
            ctx.writeAndFlush("角色名存在！\n");
            return;
        }
        PlayerExample playerExample = new PlayerExample();
        PlayerExample.Criteria criteria = playerExample.createCriteria();
        criteria.andNameEqualTo(roleName);
        List<Player> playerList = playerMapper.selectByExample(playerExample);
        if (playerList.size() > 0) {
            Player newPlayer = playerList.get(0);
            Bag bag = bagManager.newBag(newPlayer);
            bagMapper.insertSelective(bag);
            ctx.writeAndFlush("角色创建成功，角色id:" + newPlayer.getRoleid() + "角色昵称为：" + newPlayer.getName() + '\n');
        } else {
            ctx.writeAndFlush("角色创建失败！\n");
        }

    }


    /**
     * 显示人物状态
     */
    public void showState(ChannelHandlerContext ctx, PlayerModel playerModel) {
        ctx.writeAndFlush("您当前hp:[" + playerModel.getHp() + "]\n" +
                "您当前mp:[" + playerModel.getMp() + "]\n" +
                "您当前等级为:[" + playerModel.getLevel() + "]\n" +
                "您当前攻击力为:[" + playerModel.getAtk() + "]\n" +
                "您当前防御力为:[" + playerModel.getDef() + "]\n");
    }

    /**
     * 攻击怪物
     */
    public void attackMonster(PlayerModel playerModel, int skillId, int target) {
        Scene scene = playerModel.getNowScene();
        Monster monster = scene.getSceneExcel().getMonsters().get(target);
        Skill skill = playerModel.getSkillMap().get(skillId);
        skill.setNowTime(System.currentTimeMillis());
        /**判断目标是否死亡*/
        if (monster.getSelfHp() <= 0) {
            playerModel.getChannelHandlerContext().writeAndFlush("攻击目标无效，请重新选择！\n");
            return;
        }
        /**判断技能CD*/
        if (skill.getLastTime() == null || skill.getNowTime() - skill.getLastTime() >= skill.getSkillExcel().getCd() * 1000) {
            /**判断玩家mp*/
            if (playerModel.getMp() >= skill.getSkillExcel().getMp()) {
                /**判断装备磨损度*/
                for (Integer key : playerModel.getEquipMap().keySet()) {
                    if (playerModel.getEquipMap().get(key).getNowWear() <= 10) {
                        StringBuilder eqpBreak = new StringBuilder("装备损坏过于严重！请维修\n");
                        notifyScene.notifyPlayer(playerModel, eqpBreak);
                        return;
                    }
                    playerModel.getEquipMap().get(key).setNowWear(playerModel.getEquipMap().get(key).getNowWear() - 2);
                }
                int hurt = playerModel.getAtk() * skill.getSkillExcel().getBuff();
                monster.setSelfHp(monster.getSelfHp() - hurt);
                playerModel.setMp(playerModel.getMp() - skill.getSkillExcel().getMp());
                skill.setLastTime(System.currentTimeMillis());
                if (monsterService.isMonsterDeath(monster)) {
                    StringBuilder notify = new StringBuilder();
                    notify.append("玩家[").append(playerModel.getName()).append("]成功击杀怪物").append(monster.getMonsterExcel().getName()).append('\n');
                    notifyScene.notifyScene(scene, notify);
                    String[] drops = monster.getMonsterExcel().getDrop().split(",");
                    int index = (int) (Math.random() * drops.length);
                    Item item = new Item();
                    item.setId(Integer.parseInt(drops[index]));
                    item.setNowWear(item.getItemById().getWear());
                    itemService.getItem(item, playerModel);

                } else {
                    StringBuilder notify = new StringBuilder();
                    notify.append("玩家[").append(playerModel.getName()).append("]释放了技能[").append(skill.getSkillExcel().getName()).append("]对怪物[")
                            .append(monster.getMonsterExcel().getName()).append("]产生伤害:").append(hurt).append('\n');
                    notifyScene.notifyScene(scene, notify);
                    int[] param = monsterService.monsterAtk(monster);
                    int beHurt = param[0];
                    Skill monSkill = new Skill();
                    monSkill.setSkillId(param[1]);
                    StringBuilder sb = new StringBuilder();
                    sb.append("[").append(monster.getMonsterExcel().getName()).append("]使用技能[").append(monSkill.getSkillExcel().getName())
                            .append("]  您受到了:[").append(beHurt).append("]点的伤害\n");
                    notifyScene.notifyPlayer(playerModel, sb);
                    playerModel.setHp(playerModel.getHp() - beHurt);
                    if (isPlayerDeath(playerModel)) {
                        StringBuilder die = new StringBuilder("已阵亡！请复活\n");
                        notifyScene.notifyPlayer(playerModel, die);
                    }
                }
                /**计时怪物刷新*/
                Timer timer = new Timer();
                Time t = new Time();
                t.setPlayerModel(playerModel);
                timer.schedule(t, 0, 3000);
            } else {
                StringBuilder noMp = new StringBuilder("技能释放失败！原因：mp不足！\n");
                notifyScene.notifyPlayer(playerModel, noMp);
            }

        } else {
            StringBuilder waitCd = new StringBuilder("技能[").append(skill.getSkillExcel().getName()).append("]冷却中\n");
            notifyScene.notifyPlayer(playerModel, waitCd);
        }
    }


    public boolean isPlayerDeath(PlayerModel playerModel) {
        return playerModel.getHp() <= 0;
    }


}
