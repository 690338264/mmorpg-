package com.function.player.manager;

import com.function.player.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author Catherine
 * @create 2020-08-13 14:46
 */
@Component
public class PlayerCache {
    @Autowired
    private RedisTemplate<String, Player> redisTemplate;

    public void setRedisTemplate(RedisTemplate<String, Player> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

//    /**
//     * 普通缓存获取
//     * @param key 键
//     * @return 值
//     */
//    public Object get(String key){
//        System.out.println("从缓存中取出");
//        return key==null?null:redisTemplate.opsForValue().get(key);
//    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, Player value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            System.out.println(value + "已放入缓存中");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean set(String key, Player value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
