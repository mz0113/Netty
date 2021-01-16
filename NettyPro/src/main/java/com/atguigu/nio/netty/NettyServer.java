package com.atguigu.nio.netty;

import com.atguigu.nio.netty.handler.NettyServerHandler2;
import com.atguigu.nio.netty.handler.NettyServerHandler3;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import com.atguigu.nio.netty.handler.NettyServerHandler;

public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 2)//设置线程队列得到连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true)//设置连接保持活动状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {//创建一个通道测试对象
                        /**
                         * 给pipeLine设置处理器
                         * @param ch
                         * @throws Exception
                         */
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //ch.pipeline().addLast(new NettyServerHandler3());
                            ch.pipeline().addLast(new NettyServerHandler());
                            ch.pipeline().addLast(new NettyServerHandler2());
                        }
                    });
            System.out.println("server has started...");

            //绑定一个端口并且同步，生成一个channelFuture对象
            ChannelFuture channelFuture = serverBootstrap.bind(6668).sync();

/*            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        int i = 0;
                        while (true) {
                            System.out.println(i++ + "秒");
                            Thread.sleep(1000);
                            if (i==30) {
                                break;
                            }
                        }
                        channelFuture.channel().close();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();*/
            //sync()让当前线程直接阻塞在channelFuture对象的wait()方法上。然后等待channel关闭事件发生时候，netty会调用channelFuture对象的notify方法唤醒阻塞的主线程
            //closeFuture()不是去关闭连接，它只是返回一个closeFuture事件的回调对象而已，没有任何其他逻辑，等事件真正发生，netty会调用该对象的回调方法。
            channelFuture.channel().closeFuture().sync();
        }catch (Exception ex){
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
