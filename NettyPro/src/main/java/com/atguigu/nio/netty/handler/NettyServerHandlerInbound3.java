package com.atguigu.nio.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyServerHandlerInbound3 extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("i am server handler 3 in bound channelRead"+msg);
        ctx.pipeline().writeAndFlush("msg from in3");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("handler3 收到异常："+cause.getMessage());;
        cause.printStackTrace();
    }
}
