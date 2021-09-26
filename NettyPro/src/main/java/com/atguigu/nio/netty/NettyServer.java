package com.atguigu.nio.netty;

import com.atguigu.nio.netty.handler.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.util.AttributeKey;
import io.netty.util.ByteProcessor;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)//指定了channel的实现类
                    .option(ChannelOption.SO_BACKLOG, 2)//设置线程队列得到连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true)//设置连接保持活动状态
                    .childAttr(AttributeKey.newInstance("id"),1)
                    .childHandler(new ChannelInitializer<SocketChannel>() {//创建一个通道测试对象
                        /**
                         * 给pipeLine设置处理器
                         * @param ch
                         * @throws Exception
                         */
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyServerHandlerOutbound4());
                            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(9999999, Unpooled.wrappedBuffer(",".getBytes(StandardCharsets.UTF_8))));
                            ch.pipeline().addLast(new ByteToMessageDecoder() {
                                @Override
                                protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
                                    CharSequence sequence = byteBuf.readCharSequence(byteBuf.readableBytes(), StandardCharsets.UTF_8);
                                    list.add(sequence.toString());
                                }
                            });
                            ch.pipeline().addLast(new NettyServerHandlerInbound());
                            ch.pipeline().addLast(new NettyServerHandlerInbound2());
                            ch.pipeline().addLast(new NettyServerHandlerOutbound2());
                            ch.pipeline().addLast(new NettyServerHandlerOutbound3());
                            ch.pipeline().addLast(new NettyServerHandlerInbound3());
                        }
                    });
            System.out.println("server has started...");

            //绑定一个端口并且同步，生成一个channelFuture对象
            ChannelFuture channelFuture = serverBootstrap.bind(6668).sync();
            //sync()让当前线程直接阻塞在channelFuture对象的wait()方法上。然后等待channel关闭事件发生时候，netty会调用channelFuture对象的notify方法唤醒阻塞的主线程
            //closeFuture()不是去关闭连接，它只是返回一个closeFuture事件的回调对象而已，没有任何其他逻辑，等事件真正发生，netty会调用该对象的回调方法。
            channelFuture.channel().closeFuture().sync();
        }catch (Exception ex){
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
