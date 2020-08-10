package com.database.mapper;

import com.database.entity.Bag;
import com.database.entity.BagExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BagMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table bag
     *
     * @mbg.generated Mon Aug 10 12:46:49 CST 2020
     */
    long countByExample(BagExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table bag
     *
     * @mbg.generated Mon Aug 10 12:46:49 CST 2020
     */
    int deleteByExample(BagExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table bag
     *
     * @mbg.generated Mon Aug 10 12:46:49 CST 2020
     */
    int insert(Bag record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table bag
     *
     * @mbg.generated Mon Aug 10 12:46:49 CST 2020
     */
    int insertSelective(Bag record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table bag
     *
     * @mbg.generated Mon Aug 10 12:46:49 CST 2020
     */
    List<Bag> selectByExample(BagExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table bag
     *
     * @mbg.generated Mon Aug 10 12:46:49 CST 2020
     */
    int updateByExampleSelective(@Param("record") Bag record, @Param("example") BagExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table bag
     *
     * @mbg.generated Mon Aug 10 12:46:49 CST 2020
     */
    int updateByExample(@Param("record") Bag record, @Param("example") BagExample example);
}