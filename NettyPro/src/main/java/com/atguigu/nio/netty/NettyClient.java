package com.atguigu.nio.netty;

import com.atguigu.nio.netty.handler.client.NettyClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.MessageToByteEncoder;

public class NettyClient {
    public static void main(String[] args) {
        //客户端需要一个事件循环组
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        //创建客户端启动对象
        Bootstrap bootstrap = new Bootstrap();
        try {
            //设置参数
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //默认的Netty频道只能发送 ByteBuf s和 FileRegion s。您需要通过向管道添加更多处理程序或将其手动转换为 ByteBuf s将对象转换为这些类型
                            ch.pipeline().addLast(new MessageToByteEncoder() {
                                @Override
                                protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
                                    byteBuf.writeBytes(((byte[]) o));
                                }
                            });
                            ch.pipeline().addLast(new NettyClientHandler());
                        }
                    });
            System.out.println("client has started");

            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 6668).sync();

            //给关闭通道添加监听
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            System.out.println("客户端关闭连接");
            eventLoopGroup.shutdownGracefully();
        }
    }
}
