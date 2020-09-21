package com;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author Catherine
 */
public class EchoClientHandler extends ChannelInboundHandlerAdapter {
    /**
     * 监听服务器发送的数据
     */
    @Override
    public void channelRead(ChannelHandlerContext cxt, Object msg) {
        //记录已接受消息的转储
        System.out.println(msg.toString());
        MainView.OUTPUT.append(msg.toString() + "\n");
//        MainView.PLAYER_INFO.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if (MainView.PLAYER_INFO.equals(e.getSource())){
//                cxt.writeAndFlush("8888\n");}
//            }
//        });
        MainView.OUTPUT.setCaretPosition(MainView.OUTPUT.getDocument().getLength());
    }

    /**
     * 启动客户端时触发
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client active");
        super.channelActive(ctx);
    }

    /**
     * 关闭客户端触发
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client close");
        super.channelInactive(ctx);
    }

}
