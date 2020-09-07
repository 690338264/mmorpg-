package com.handler;

import com.Cmd;
import com.function.user.service.UserService;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Catherine
 */
@Component
@Slf4j
public class ControllerManager {
    @Autowired
    private UserService userService;

    private static ControllerManager self;

    private final static Map<Cmd, Controller> CONTROLLER_MAP = new ConcurrentHashMap<>();

    private final static Map<Cmd, LoggedInController> LOGGED_CONTROLLER_MAP = new ConcurrentHashMap<>();

    @PostConstruct
    private void init() {
        self = this;
        self.userService = this.userService;
    }

    public static void add(Cmd cmd, Controller contr) {
        CONTROLLER_MAP.put(cmd, contr);
    }

    public static void add(Cmd cmd, LoggedInController controller) {
        LOGGED_CONTROLLER_MAP.put(cmd, controller);
    }

    public Controller get(int cmdId) {
        return CONTROLLER_MAP.get(Cmd.find(cmdId, Cmd.UNKNOWN));
    }

    public LoggedInController gets(int cmdId) {
        return LOGGED_CONTROLLER_MAP.get(Cmd.find(cmdId, Cmd.UNKNOWN));
    }

    /**
     * @param contr 要执行的任务
     * @param ctx   上下文
     */

    public void execute(Controller contr, ChannelHandlerContext ctx) {

    }

    public static ControllerManager getSelf() {
        return self;
    }

    public static UserService getUserService() {
        return self.userService;
    }
}
