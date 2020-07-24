package com.database.mapper;

import com.database.entity.Sect;
import com.database.entity.SectExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SectMapper {
    long countByExample(SectExample example);

    int deleteByExample(SectExample example);

    int insert(Sect record);

    int insertSelective(Sect record);

    List<Sect> selectByExample(SectExample example);

    int updateByExampleSelective(@Param("record") Sect record, @Param("example") SectExample example);

    int updateByExample(@Param("record") Sect record, @Param("example") SectExample example);
}