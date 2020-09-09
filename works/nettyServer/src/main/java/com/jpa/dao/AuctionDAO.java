package com.jpa.dao;

import com.jpa.entity.TAuction;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Catherine
 * @create 2020-09-08 11:56
 */
public interface AuctionDAO extends JpaRepository<TAuction, Long> {
}
