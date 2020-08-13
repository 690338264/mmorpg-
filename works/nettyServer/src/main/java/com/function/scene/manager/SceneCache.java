package com.function.scene.manager;

import com.function.scene.model.Scene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author Catherine
 * @create 2020-08-13 15:56
 */
@Component
public class SceneCache {
    @Autowired
    private RedisTemplate<String, Scene> redisTemplate;

    public void setRedisTemplate(RedisTemplate<String, Scene> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 普通缓存放入
     *
     * @param scene 键
     * @return true成功 false失败
     */
    public boolean set(Scene scene) {
        try {
            redisTemplate.opsForValue().set(scene.getSceneExcel().getName(), scene);
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
    public Object get(Integer key) {
        System.out.println("从缓存中取出");
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

}
