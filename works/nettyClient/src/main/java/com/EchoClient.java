package com;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;

/**
 * @author Catherine
 */
public class EchoClient {
    private static String host = "127.0.0.1";

    public EchoClient() {
    }

    public EchoClient(String ip) {
        host = ip;
    }

    public static Channel channel = null;

    private long restTime = 1000L;

    private static final int Port = 8000;

    static {
        MainView mainView = new MainView();
    }

    public void run() {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(workerGroup)
                .channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress(host, Port));
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline()
                        .addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()))
                        .addLast("decoder", new StringDecoder())
                        .addLast("encoder", new StringEncoder())
                        .addLast(new EchoClientHandler());
                channel = ch;
            }

        });
        try {
            //连接服务器
            Channel channel = b.connect(host, Port).sync().channel();
            loop();//循环监听输入
        } catch (Exception e) {
            try {
                Thread.sleep(restTime);
                restTime *= 2;
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            System.out.println("error!!");
            e.printStackTrace();
            System.out.println("尝试重连");
            run();
        }
    }

    private void loop() throws IOException {
        System.out.println("----连接服务器[" + host + "]Success!当前连接的是[" + channel.id() + "]-----\n");
        System.out.println(("请输入1001 id 密码登陆，如无账号请输入1000 用户名 密码来注册\n"));
        while (true) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String content = reader.readLine();
            System.out.println("客户端输入：" + content);
            if (StringUtils.isNotEmpty(content)) {
                if (StringUtils.equalsIgnoreCase(content, "q")) {
                    System.exit(1);
                } else {
                    channel.writeAndFlush(content + '\n');
                }
            }
        }
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            host = args[0];
        }
        new EchoClient(host).run();

    }
}
