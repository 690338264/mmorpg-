package com.jpa.dao;

import com.jpa.entity.TUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Catherine
 * @create 2020-08-11 17:26
 */
public interface UserDAO extends JpaRepository<TUser, Long> {
    TUser findByName(String name);

    TUser findByIdAndPsw(long userId, String psw);
}
