package com.jpa.dao;

import com.jpa.entity.TBag;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Catherine
 * @create 2020-08-11 21:35
 */
public interface BagDAO extends JpaRepository<TBag, Long> {
    /**
     * 根据playerId查找
     *
     * @param roleId 角色id
     * @return 背包
     */
    TBag findByPlayerId(Long roleId);

}
