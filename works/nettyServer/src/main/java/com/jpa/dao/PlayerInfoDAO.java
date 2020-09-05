package com.jpa.dao;

import com.jpa.entity.TPlayerInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Catherine
 * @create 2020-09-05 21:24
 */
public interface PlayerInfoDAO extends JpaRepository<TPlayerInfo, Long> {
}
