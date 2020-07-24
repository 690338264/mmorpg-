package com.database.mapper;

import com.database.entity.Player;
import com.database.entity.PlayerExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PlayerMapper {
    long countByExample(PlayerExample example);

    int deleteByExample(PlayerExample example);

    int deleteByPrimaryKey(String name);

    int insert(Player record);

    int insertSelective(Player record);

    List<Player> selectByExample(PlayerExample example);

    Player selectByPrimaryKey(String name);

    int updateByExampleSelective(@Param("record") Player record, @Param("example") PlayerExample example);

    int updateByExample(@Param("record") Player record, @Param("example") PlayerExample example);

    int updateByPrimaryKeySelective(Player record);

    int updateByPrimaryKey(Player record);
}