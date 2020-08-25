package com.function.skill.cache;

import com.function.skill.model.Skill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author Catherine
 * @create 2020-08-25 16:30
 */
@Component
public class SkillCache {
    @Autowired
    private RedisTemplate<String, Skill> redisTemplate;

    public void setRedisTemplate(RedisTemplate<String, Skill> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, Skill value) {
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
     * @param key 键
     * @return 值
     */
    public Skill get(String key) {
        System.out.println("从缓存中取出");
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }
}
