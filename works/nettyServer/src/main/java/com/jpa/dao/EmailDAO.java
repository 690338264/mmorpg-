package com.jpa.dao;

import com.jpa.entity.TEmail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Catherine
 * @create 2020-09-02 15:58
 */
public interface EmailDAO extends JpaRepository<TEmail, Long> {

    /**
     * 根据玩家id查找邮件
     *
     * @param playerId 玩家id
     * @return 邮件
     */
    List<TEmail> findByPlayerId(Long playerId);

}
