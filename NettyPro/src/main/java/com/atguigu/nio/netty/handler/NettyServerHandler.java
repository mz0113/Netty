package com.atguigu.nio.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;


/**
 * 这些都不是单例的，一个连接会new一个新的handler，不同连接之间不共享对象
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * 读取数据
     * @param ctx 上下文对象，含有管道pipeLine和通道channel
     * @param msg (ByteBuf)客户端发送来的数据
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf)msg;//性能更高，netty封装
        System.out.println("server read = " + byteBuf.toString(CharsetUtil.UTF_8));

       // System.out.println("开始执行异步任务");

/*        //定时任务在客户端连接断开之后仍会继续工作
        ctx.channel().eventLoop().submit(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        System.out.println("正在执行异步任务"+Thread.currentThread().getName());
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //定时任务在客户端连接断开之后仍会继续工作
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        System.out.println("正在执行定时任务"+Thread.currentThread().getName());
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        },5, TimeUnit.SECONDS);*/
    }

    /**
     * 数据读取完毕
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("server read complete");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
    }
}
