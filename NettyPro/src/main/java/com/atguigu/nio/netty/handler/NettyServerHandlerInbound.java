package com.atguigu.nio.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;


/**
 * 这些都不是单例的，一个连接会new一个新的handler，不同连接之间不共享对象
 */
public class NettyServerHandlerInbound extends ChannelInboundHandlerAdapter {
    /**
     * 读取数据
     * @param ctx 上下文对象，含有管道pipeLine和通道channel
     * @param msg (ByteBuf)客户端发送来的数据
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("i am server handler 1 in bound channelRead "+msg);
        ctx.fireChannelRead("被入栈1包装");
    }

}
