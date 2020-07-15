package com;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

public class EchoServer {
    //private int port;

    /*public EchoServer(int port){
        this.port = port;
    }*/

    public static void main(String[] args){


    /*启动方法
    @throws InterruptedException
     */

        //final EchoServerHandler serverHandler = new EchoServerHandler();
        //1创建EventLoopGroup用来接收和处理新的连接
        EventLoopGroup bossgroup = new NioEventLoopGroup();
        EventLoopGroup workergroup = new NioEventLoopGroup();
        try {
            //2 创建ServerBootstrap
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossgroup,workergroup)
                    //3 指定channel类型
                    .channel(NioServerSocketChannel.class)
                    //4 使用指定的端口设置套接字地址
                    //.localAddress(new InetSocketAddress((port)))
                    //5 添加一个EchoServerHandler到子channel的ChannelPipeLine 指定Handler
                    .childHandler(new ChannelInitializer<SocketChannel>(){

                        @Override
                        public void initChannel(SocketChannel socketChannel)
                            throws  Exception{
                            /*EchoServerHandler被标注为@Shareable，所以我们可以总是使用同样的实例
                            socketChannel.pipeline().addLast(serverHandler);*/
                            //以"\n"为结尾分割的解码器  *DelimiterBasedFrameDecoder*
                            socketChannel.pipeline().addLast("framer",new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
                            //字符串解码和编码  默认的StringDecoder字符串形式输出
                            socketChannel.pipeline().addLast("decoder",new StringDecoder());
                            socketChannel.pipeline().addLast("encoder",new StringEncoder());

                            socketChannel.pipeline().addLast(new EchoServerHandler());
                        }
                    }).option(ChannelOption.SO_KEEPALIVE,true);
            //6 异地绑定服务器；调用sync方法阻塞等待直到绑定完成
            ChannelFuture f = b.bind(8000).sync();  //绑定8000端口
            //7 获取channel的closeFuture，并且阻塞当前线程直到它完成
            f.channel().closeFuture().sync();
        }
        catch (InterruptedException e){}
        finally {
            //8 关闭EventLoopGroup释放所有资源
            workergroup.shutdownGracefully();
            bossgroup.shutdownGracefully();
        }

    }
}
