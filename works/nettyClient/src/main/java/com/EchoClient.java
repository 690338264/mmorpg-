package com;

//import com.sun.deploy.util.StringUtils;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;

public class EchoClient {
    private static String host = "127.0.0.1";

    public EchoClient() {
    }

    public EchoClient(String ip) {
        host = ip;
    }

    public static Channel channel = null;

    private long restTime = 1000L;//重启时间

    private static int Port = 8000;

    public void run() {
        EventLoopGroup workerGroup = new NioEventLoopGroup();//多线程循环
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
                /*Client adapter*/
                channel = ch;
            }

        });
        try {
            Channel channel = b.connect(host, Port).sync().channel();//连接服务器
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
        while (true) {
            System.out.println("请输入您的操作：");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String content = reader.readLine();
            System.out.println("客户端输入：" + content);
            if (StringUtils.isNotEmpty(content)) {
                if (StringUtils.equalsIgnoreCase(content, "q")) {
                    System.exit(1);

                    //String[] array = content.split(" ");
                    //CMD连接

                }
                else {
                    channel.writeAndFlush(content+'\n');
                }
            }
        }
    }

    /*public void start() throws InterruptedException{
        //事件处理分配，包括创建新的连接以及处理入站和出站数据
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            //创建Bootstrap 初始化客户端
            Bootstrap b = new Bootstrap();

            b.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host,port))
                    .handler(new ChannelInitializer<SocketChannel>(){
                        @Override
                        protected void initChannel(SocketChannel socketChannel)
                            throws Exception{
                            socketChannel.pipeline().addLast(new EchoClientHandler());
                        }
                    });
            //连接到远程节点，阻塞等待直到连接完成
            ChannelFuture f = b.connect().sync();
            //阻塞，直至channel关闭
            f.channel().closeFuture().sync();
        }finally {
            //关闭线程池并且释放所有资源
            group.shutdownGracefully().sync();
        }
    }*/
    /*public static void main(String[] args){
        //Bootstrap
        Bootstrap b = new Bootstrap();
        //指定channel类型
        b.channel(NioSocketChannel.class);
        //指定Handler
        b.handler(new ChannelInitializer<Channel>() {

            @Override
            protected void initChannel(Channel ch) throws Exception{
             ChannelPipeline pipeline = ch.pipeline();

             pipeline.addLast("framer",new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
             pipeline.addLast("decoder",new StringDecoder());
             pipeline.addLast("encoder",new StringEncoder());

             //客户端逻辑
             pipeline.addLast(new EchoClientHandler());
            }
        });

        //指定EventLoopGroup[事件组]
        b.group(new NioEventLoopGroup());
        //连接到本地8000端口的服务端0
        b.connect(new InetSocketAddress("127.0.0.1",8000));

    }*/
    public static void main(String[] args) {
        if (args.length > 0) {
            host = args[0];
        }
        new EchoClient(host).run();

    }
}
