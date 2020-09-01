package com.function.occ.manager;

import com.function.occ.excel.OccExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author Catherine
 * @create 2020-08-13 20:20
 */
@Component
public class OccCache {
    @Autowired
    private RedisTemplate<String, OccExcel> redisTemplate;

    public void setRedisTemplate(RedisTemplate<String, OccExcel> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, OccExcel value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            System.out.println(value + "已放入缓存中");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 普通缓存获取
     *
     * @param id 键
     * @return 值
     */
    public OccExcel get(int id) {
        String key = "Occ" + id;
        System.out.println("从缓存中取出");
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }
}
