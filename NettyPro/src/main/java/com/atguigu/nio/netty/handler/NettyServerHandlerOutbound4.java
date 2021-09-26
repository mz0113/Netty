package com.atguigu.nio.netty.handler;

import io.netty.channel.*;

public class NettyServerHandlerOutbound4 extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        System.out.println("i am out bound hander 4"+msg);

        ChannelFuture channelFuture = ctx.write(msg + "被outbound4包装");
    }
}
