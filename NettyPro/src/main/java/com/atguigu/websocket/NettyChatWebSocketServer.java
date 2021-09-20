package com.atguigu.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * 基于webSocket的在线聊天室 /resource目录
 */
public class NettyChatWebSocketServer {
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
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 获取管道，将一个一个的ChannelHandler添加到管道中
                            ChannelPipeline pipeline = ch.pipeline();

                            // 添加一个http的编解码器
                            pipeline.addLast(new HttpServerCodec());
                            // 添加一个用于支持大数据流的支持
                            pipeline.addLast(new ChunkedWriteHandler());
                            // 添加一个聚合器，这个聚合器主要是将HttpMessage聚合成FullHttpRequest/Response
                            pipeline.addLast(new HttpObjectAggregator(1024*64));
                            // 需要指定接收请求的路由
                            // 必须使用以ws后缀结尾的url才能访问
                            pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
                            // 添加自定义的Handler
                            pipeline.addLast(new ChatHandler());

                            // 增加心跳事件支持
                            // 第一个参数:  读空闲4秒
                            // 第二个参数： 写空闲8秒
                            // 第三个参数： 读写空闲12秒
                            pipeline.addLast(new IdleStateHandler(4,8,12));
                            pipeline.addLast(new HeartBeatHandler());
                        }
                    });
            System.out.println("server has started...");

            //绑定一个端口并且同步，生成一个channelFuture对象
            ChannelFuture channelFuture = serverBootstrap.bind(9090).sync();
            //sync()让当前线程直接阻塞在channelFuture对象的wait()方法上。然后等待channel关闭事件发生时候，netty会调用channelFuture对象的notify方法唤醒阻塞的主线程
            //closeFuture()不是去关闭连接，它只是返回一个closeFuture事件的回调对象而已，没有任何其他逻辑，等事件真正发生，netty会调用该对象的回调方法。
            channelFuture.channel().closeFuture().sync();
        }catch (Exception ex){
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
