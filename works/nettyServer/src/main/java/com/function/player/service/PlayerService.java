package com.function.player.service;

import com.database.entity.Player;
import com.database.entity.PlayerExample;
import com.database.mapper.PlayerMapper;
import com.function.monster.model.Monster;
import com.function.monster.service.MonsterService;
import com.function.player.model.OccResource;
import com.function.player.model.Occupation;
import com.function.player.model.PlayerModel;
import com.function.scene.model.Scene;
import com.function.skill.model.Skill;
import com.function.skill.model.SkillResource;
import com.manager.Notify;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class PlayerService {
    @Autowired
    private PlayerMapper playerMapper;
    @Autowired
    private MonsterService monsterService;
    @Autowired
    private Notify notify;

    public void roleCreate(ChannelHandlerContext ctx, String roleName, Integer roleType, Long userId) {
        Player player = new Player();
        player.setName(roleName);
        player.setOccupation(roleType);
        player.setId(userId);
        player.setLoc(1);
        player.setExp(1);
        Random r = new Random();
        int rom = r.nextInt();
        Long roleId = new Long((long) rom);
        player.setRoleid(roleId);
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

    public void attackMonster(ChannelHandlerContext ctx, PlayerModel playerModel, int skillId, int target) {
        Scene scene = playerModel.getNowScene();
        Monster monster = scene.getMonsters().get(target);
        Skill skill = playerModel.getSkillMap().get(skillId);
        skill.setStatus(0);
        int hurt = playerModel.getAtk()*skill.getBuff();
        monster.setSelfHp(monster.getMonsterExcel().getHp()-hurt);
        playerModel.setMp(playerModel.getMp()-skill.getMp());
        if(monsterService.isMonsterDeath(monster)){
            notify.notifyScene(scene,"玩家["+playerModel.getName()+"]成功击杀怪物"+monster.getMonsterExcel().getName()+'\n');
        }else{
            notify.notifyScene(scene,"玩家["+playerModel.getName()+"]对怪物["+monster.getMonsterExcel().getName()+"]产生伤害:"+hurt+'\n');
        }
        int beHurt = monsterService.monsterAtk(monster);
        ctx.writeAndFlush("您收到了:["+beHurt+"]点的伤害\n");
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
        Occupation occ = OccResource.getOccById(playerModel.getOccupation());
        playerModel.setHp(occ.getHp());
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
            Skill skill = SkillResource.getSkillById(type);
            playerModel.getSkillMap().put(i + 1, skill);
        }
    }

    public void initPlayer(PlayerModel playerModel) {
        initLevel(playerModel);
        initAttribute(playerModel);
        initSkill(playerModel);
    }

}
