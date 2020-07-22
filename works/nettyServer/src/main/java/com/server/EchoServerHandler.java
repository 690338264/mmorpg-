package com.server;

import com.handler.controller;
import com.handler.controllerManager;
import com.handler.errorController;
import io.netty.channel.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import util.Msg;

import javax.annotation.Resource;
import java.net.InetAddress;
//对数据的处理

@Slf4j
@ChannelHandler.Sharable

public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    @Resource
    private controllerManager contrManager;
    @Resource
    private errorController errController;
    private String cmd;
    private int cmdID;

    @Override
        public void channelRead(ChannelHandlerContext ctx,Object msg) {
        ctx.writeAndFlush("server Received your message !\n");
        Channel ch = ctx.channel();
        cmd = msg.toString();
        String[] split = cmd.split(" ");
        String cmdIDs = split[0];
        cmdID = Integer.parseInt(cmdIDs.trim());
        ctx.writeAndFlush("yeooo"+cmdID+'\n');
        controller contr = contrManager.get(cmdID);
        Msg message = new Msg();
        message.setCmdId(cmdID);
        message.setContent(cmd);

        if (contr == null){
            errController.handle(ctx,message);
        }else{
            contrManager.execute(contr,ctx);
        }

//        System.out.println(cmd);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx)throws Exception{
        System.out.println("RamoteAddress:"+ctx.channel().remoteAddress()+"active!");
        ctx.writeAndFlush("Welcome to "+ InetAddress.getLocalHost().getHostName()+"service!\n");//回复
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx)throws Exception{
        ctx.writeAndFlush("正在断开连接\n");
        /*保存数据*/
        log.info("客户端已离线");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause){
        log.error("服务器异常");
        /*保存角色信息*/
    }

    public void setCmdID(int cmdID) {
        this.cmdID = cmdID;
    }

    public int getCmdID() {
        return cmdID;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }
}
