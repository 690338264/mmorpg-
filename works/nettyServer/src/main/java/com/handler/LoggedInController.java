package com.handler;

import com.function.player.model.Player;
import util.Msg;

/**
 * @author Catherine
 * @create 2020-09-01 18:07
 */
@FunctionalInterface
public interface LoggedInController {
    /**
     * 接收数据处理业务
     *
     * @param player  登陆玩家
     * @param message 指令信息
     */
    void handle(Player player, Msg message);
}
