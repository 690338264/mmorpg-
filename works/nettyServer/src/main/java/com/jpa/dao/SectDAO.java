package com.jpa.dao;

import com.jpa.entity.TSect;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Catherine
 * @create 2020-09-05 02:06
 */
public interface SectDAO extends JpaRepository<TSect, Long> {
    TSect findByName(String name);
}
