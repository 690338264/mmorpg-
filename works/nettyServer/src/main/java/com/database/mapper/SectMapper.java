package com.database.mapper;

import com.database.entity.Sect;
import com.database.entity.SectExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SectMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sect
     *
     * @mbg.generated Tue Aug 04 10:26:58 CST 2020
     */
    long countByExample(SectExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sect
     *
     * @mbg.generated Tue Aug 04 10:26:58 CST 2020
     */
    int deleteByExample(SectExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sect
     *
     * @mbg.generated Tue Aug 04 10:26:58 CST 2020
     */
    int insert(Sect record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sect
     *
     * @mbg.generated Tue Aug 04 10:26:58 CST 2020
     */
    int insertSelective(Sect record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sect
     *
     * @mbg.generated Tue Aug 04 10:26:58 CST 2020
     */
    List<Sect> selectByExample(SectExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sect
     *
     * @mbg.generated Tue Aug 04 10:26:58 CST 2020
     */
    int updateByExampleSelective(@Param("record") Sect record, @Param("example") SectExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sect
     *
     * @mbg.generated Tue Aug 04 10:26:58 CST 2020
     */
    int updateByExample(@Param("record") Sect record, @Param("example") SectExample example);
}