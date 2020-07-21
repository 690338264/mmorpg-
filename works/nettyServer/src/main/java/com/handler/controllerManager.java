package com.handler;

import com.Function.Player.PlayerDataServer;
import com.Cmd;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class controllerManager{
    @Resource
    private PlayerDataServer playerDataServer;

    private final static Map<Cmd,controller> CONTROLLER_MAP = new ConcurrentHashMap<>();

    public static void add(Cmd cmd,controller contr){
        CONTROLLER_MAP.put(cmd,contr);
    }

    public controller get(int cmdID){
        return CONTROLLER_MAP.get(Cmd.find(cmdID,Cmd.UNKNOWN));
    }
    /**
     *
     * @param contr   要执行的任务
     * @param ctx   上下文
     */

    public void execute(controller contr, ChannelHandlerContext ctx){

    }


}
