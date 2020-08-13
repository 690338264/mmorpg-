package com.jpa.dao;

import com.jpa.entity.TUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Catherine
 * @create 2020-08-11 17:26
 */
public interface UserDAO extends JpaRepository<TUser, Long> {
    /**
     * 根据用户名查找
     *
     * @param name 用户名
     * @return 用户
     */
    TUser findByName(String name);

    /**
     * 根据用户id和用户名查找
     *
     * @param userId 用户id
     * @param psw    密码
     * @return 用户
     */
    TUser findByIdAndPsw(long userId, String psw);
}
