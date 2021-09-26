package com.atguigu.nio.netty.handler.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    /**
     * 通道就绪
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client channel active");
        //一个汉字3个字节。
        byte[] bytes = "我来自,客,户端awd,1,2,5".getBytes(StandardCharsets.UTF_8);
        ChannelFuture channelFuture = ctx.writeAndFlush(bytes);
        System.out.println(new String(new byte[]{-26,-120,-111},StandardCharsets.UTF_8));

        for (byte aByte : bytes) {
            System.out.println(aByte);
        }
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                Void unused = channelFuture.get();
                System.out.println(channelFuture.isSuccess());
            }
        });
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = ((ByteBuf) msg);
        System.out.println("client receive:" + byteBuf.toString(CharsetUtil.UTF_8));
    }



    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client read complete");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
    }
}
