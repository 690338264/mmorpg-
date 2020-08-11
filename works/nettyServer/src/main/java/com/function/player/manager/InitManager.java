package com.function.player.manager;

import com.function.player.model.PlayerModel;

/**
 * @author Catherine
 * @create 2020-08-06 18:34
 */
public interface InitManager {
    /**
     * 加载数据库
     *
     * @param playerModel
     * @return
     */
    Object init(PlayerModel playerModel);
}
