package com.function.player.service;

import com.database.entity.Player;
import com.database.entity.PlayerExample;
import com.database.mapper.PlayerMapper;
import com.function.player.model.PlayerModel;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlayerService {
    private PlayerMapper playerMapper;
    public void roleCreate(ChannelHandlerContext ctx,String roleName,Integer roleType,Long userId){
        Player player = new Player();
        player.setName(roleName);
        player.setOccupation(roleType);
        player.setId(userId);
        player.setLoc(1);
        try{
            playerMapper.insertSelective(player);
        }catch (DuplicateKeyException e){
            ctx.writeAndFlush("角色名存在！\n");
            return;
        }
        PlayerExample playerExample = new PlayerExample();
        PlayerExample.Criteria criteria = playerExample.createCriteria();
        criteria.andNameEqualTo(roleName);
        List<Player> playerList = playerMapper.selectByExample(playerExample);
        if(playerList.size()>0){
            Player newPlayer = playerList.get(0);
            ctx.writeAndFlush("角色创建成功，角色id:"+newPlayer.getRoleid()+"角色昵称为："+newPlayer.getName()+'\n');
        }
        else{
            ctx.writeAndFlush("角色创建失败！");
        }

    }

    public void initLevel(PlayerModel playerModel){
        playerModel.setLevel(playerModel.getExp()/2000);
    }

    public void initPlayer(PlayerModel playerModel){
        initLevel(playerModel);

    }

}
