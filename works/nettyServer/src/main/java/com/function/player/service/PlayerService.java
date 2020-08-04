package com.function.player.service;

import com.database.entity.Bag;
import com.database.entity.Player;
import com.database.entity.PlayerExample;
import com.database.mapper.BagMapper;
import com.database.mapper.PlayerMapper;
import com.function.bag.service.BagService;
import com.function.monster.model.Monster;
import com.function.monster.service.MonsterService;
import com.function.occ.excel.OccExcel;
import com.function.occ.excel.OccResource;
import com.function.player.controller.Time;
import com.function.player.model.PlayerModel;
import com.function.scene.excel.SceneExcel;
import com.function.skill.model.Skill;
import com.manager.NotifyScene;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.Timer;

@Component
public class PlayerService {
    @Autowired
    private PlayerMapper playerMapper;
    @Autowired
    private BagMapper bagMapper;
    @Autowired
    private MonsterService monsterService;
    @Autowired
    private BagService bagService;
    @Autowired
    private NotifyScene notifyScene;

    public void roleCreate(ChannelHandlerContext ctx, String roleName, Integer roleType, Long userId) {
        Player player = new Player();
        player.setName(roleName);
        player.setOccupation(roleType);
        player.setId(userId);
        player.setLoc(1);
        player.setExp(1);
        player.setMoney(0);
        Random r = new Random();
        int rom = r.nextInt();
        Long roleId = new Long((long) rom);
        player.setRoleid(roleId);
        Bag bag = new Bag();
        bag.setPlayerid(roleId);
        bag.setVolume(36);
        try {
            playerMapper.insertSelective(player);
            bagMapper.insertSelective(bag);
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

            ctx.writeAndFlush("角色创建成功，角色id:" + newPlayer.getRoleid() + "角色昵称为：" + newPlayer.getName() + '\n');
        } else {
            ctx.writeAndFlush("角色创建失败！\n");
        }

    }

    public void updateLoc(Integer loc, PlayerModel playerModel) {
        PlayerExample playerExample = new PlayerExample();
        PlayerExample.Criteria criteria = playerExample.createCriteria();
        criteria.andRoleidEqualTo(playerModel.getRoleid());
        Player newPlayer = new Player();
        newPlayer.setLoc(loc);
        playerMapper.updateByExampleSelective(newPlayer, playerExample);
    }

    public void showState(ChannelHandlerContext ctx, PlayerModel playerModel) {
        ctx.writeAndFlush("您当前hp:[" + playerModel.getHp() + "]\n" +
                "您当前mp:[" + playerModel.getMp() + "]\n" +
                "您当前等级为:[" + playerModel.getLevel() + "]\n");
    }

    public void attackMonster(ChannelHandlerContext ctx, PlayerModel playerModel, int skillId, int target) {
        SceneExcel sceneExcel = playerModel.getNowScene();
        Monster monster = sceneExcel.getMonsters().get(target);
        Skill skill = playerModel.getSkillMap().get(skillId);
        skill.setNowTime(System.currentTimeMillis());
        if (monster.getSelfHp() <= 0) {
            ctx.writeAndFlush("攻击目标无效，请重新选择！\n");
        } else {
            if (skill.getLastTime() == null || skill.getNowTime() - skill.getLastTime() >= skill.getSkillExcel().getCd() * 1000) {
                if (playerModel.getMp() >= skill.getSkillExcel().getMp()) {

                    int hurt = playerModel.getAtk() * skill.getSkillExcel().getBuff();
                    monster.setSelfHp(monster.getSelfHp() - hurt);
                    playerModel.setMp(playerModel.getMp() - skill.getSkillExcel().getMp());
                    skill.setLastTime(System.currentTimeMillis());
                    if (monsterService.isMonsterDeath(monster)) {
                        notifyScene.notifyScene(sceneExcel, "玩家[" + playerModel.getName() + "]成功击杀怪物" + monster.getMonsterExcel().getName() + '\n');
                    } else {
                        notifyScene.notifyScene(sceneExcel, "玩家[" + playerModel.getName() + "]释放了技能[" + skill.getSkillExcel().getName()
                                + "]对怪物[" + monster.getMonsterExcel().getName() + "]产生伤害:" + hurt + '\n');
                        int[] param = monsterService.monsterAtk(monster);
                        int beHurt = param[0];
                        Skill monSkill = new Skill();
                        monSkill.setSkillId(param[1]);
                        ctx.writeAndFlush("[" + monster.getMonsterExcel().getName() + "]使用技能[" + monSkill.getSkillExcel().getName() + "]  您受到了:[" + beHurt + "]点的伤害\n");
                        playerModel.setHp(playerModel.getHp() - beHurt);
                        if (isPlayerDeath(playerModel)) {
                            ctx.writeAndFlush("已阵亡！请复活\n");
                        }
                    }
                    Timer timer = new Timer();
                    Time t = new Time();
                    t.setPlayerModel(playerModel);
                    timer.schedule(t, 0, 3000);
                } else {
                    ctx.writeAndFlush("技能释放失败！原因：mp不足！\n");
                }

            } else {
                ctx.writeAndFlush("技能[" + skill.getSkillExcel().getName() + "]冷却中\n");
            }
        }
    }

    public boolean isPlayerDeath(PlayerModel playerModel) {
        if (playerModel.getHp() <= 0) {
            return true;
        } else {
            return false;
        }
    }

    public void initLevel(PlayerModel playerModel) {
        playerModel.setLevel(playerModel.getExp() / 2000);
    }

    public void initAttribute(PlayerModel playerModel) {
        OccExcel occ = OccResource.getOccById(playerModel.getOccupation());
        playerModel.setHp(occ.getHp());
        playerModel.setOriHp(occ.getHp() + playerModel.getLevel() * occ.getHp() / 5 * 4);
        playerModel.setOriMp(occ.getMp() + playerModel.getLevel() * occ.getMp() / 5 * 4);
        playerModel.setMp(occ.getMp());
        playerModel.setAtk(occ.getAtk());
        playerModel.setDef(occ.getDef());
        playerModel.setSpeed(occ.getSpeed());
    }

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

    public void initPlayer(PlayerModel playerModel) {
        initLevel(playerModel);
        initAttribute(playerModel);
        initSkill(playerModel);
        bagService.initBag(playerModel);
    }

}
