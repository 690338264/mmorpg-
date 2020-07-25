package com.handler;

import com.function.player.service.PlayerService;
import com.Cmd;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class ControllerManager {

    private static ControllerManager self;

    @Resource
    private PlayerService playerServer;

    @PostConstruct
    private void init(){
        self = this;
    }

    private final static Map<Cmd, Controller> CONTROLLER_MAP = new ConcurrentHashMap<>();

    public static void add(Cmd cmd, Controller contr){
        CONTROLLER_MAP.put(cmd,contr);
    }

    public Controller get(int cmdID){
        return CONTROLLER_MAP.get(Cmd.find(cmdID,Cmd.UNKNOWN));
    }
    /**
     *
     * @param contr   要执行的任务
     * @param ctx   上下文
     */

    public void execute(Controller contr, ChannelHandlerContext ctx){
        ctx.writeAndFlush("111111111111111111111111111111111\n");
    }

    public static ControllerManager getSelf() {
        return self;
    }
}
