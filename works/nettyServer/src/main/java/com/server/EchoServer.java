package com.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class EchoServer {

    public void bind(int port){

        EventLoopGroup bossGroup = new NioEventLoopGroup();//逻辑
        EventLoopGroup workerGroup = new NioEventLoopGroup();//工作

        try {
            //创建ServerBootstrap
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup,workerGroup)
                    //3 指定channel类型
                    .channel(NioServerSocketChannel.class)
                    //5 添加一个EchoServerHandler到子channel的ChannelPipeLine 指定Handler
                    .childHandler(new ChannelInitializer<SocketChannel>(){

                        @Override
                        public void initChannel(SocketChannel socketChannel){
                            socketChannel.pipeline().addLast("framer",new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
                            socketChannel.pipeline().addLast("decoder",new StringDecoder());
                            socketChannel.pipeline().addLast("encoder",new StringEncoder());
                            socketChannel.pipeline().addLast(new EchoServerHandler());
                        }
                    })
                    .childOption(ChannelOption.SO_KEEPALIVE,true);
            //6 异地绑定服务器；调用sync方法阻塞等待直到绑定完成
            ChannelFuture f = b.bind(port).sync();  //绑定端口
            //7 获取channel的closeFuture，并且阻塞当前线程直到它完成
            f.channel().closeFuture().sync();
            System.out.println("ok");
        }
        catch (Exception e){throw new RuntimeException();}
        finally {
            //8 关闭EventLoopGroup释放所有资源
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
    public static void main(String[] args) throws Exception{
        new ClassPathXmlApplicationContext("applicationContext.xml");
        int port = 8000;
        new EchoServer().bind(port);
    }
    public void start(){
        int port = 8000;
        try{
            bind(port);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }


}

