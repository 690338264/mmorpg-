package com;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

public class EchoClient {
    /*private int port;
    private String host;
    public EchoClient(int port,String host){
        this.port = port;
        this.host = host;
    }
    public void start() throws InterruptedException{
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
    public static void main(String[] args){
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
        //连接到本地8000端口的服务端
        b.connect(new InetSocketAddress("127.0.0.1",8000));
        /*String host = args[0];
        int port = Integer.parseInt(args[1]);
        new EchoClient(port,host).start();*/
    }
}
