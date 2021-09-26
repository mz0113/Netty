package com.atguigu.nio.netty.handler;

import io.netty.channel.*;

public class NettyServerHandlerOutbound3 extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        System.out.println("i am out bound hander 3"+msg);
        ChannelFuture channelFuture = ctx.write(msg + "被outbound3包装");
    }
}
