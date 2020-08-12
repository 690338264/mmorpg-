package com.jpa.dao;

import com.jpa.entity.TPlayer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Catherine
 * @create 2020-08-11 20:44
 */
public interface PlayerDAO extends JpaRepository<TPlayer, Long> {
    List<TPlayer> findByUserId(Long id);

    TPlayer findByRoleId(Long playerId);

    TPlayer findByRoleIdAndUserId(Long playerId, Long id);

    TPlayer findByName(String roleName);
}
