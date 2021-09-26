package com.atguigu.nio.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyServerHandlerInbound2 extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("i am server handler 2 in bound channelRead"+msg);
        ctx.fireChannelRead("被入栈2包装");
    }
}
