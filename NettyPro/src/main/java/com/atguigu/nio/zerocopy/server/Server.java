package com.atguigu.nio.zerocopy.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * transferTo 零拷贝
 */
public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(true);
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        while (true) {
            try{
                SocketChannel socketChannel = serverSocketChannel.accept();
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);//1GB
                while (true) {
                    int read = socketChannel.read(byteBuffer);
                    if (read==-1) {
                        System.out.println("read = -1");
                        break;
                    }
                    byteBuffer.clear();
                }
            }catch (IOException ex){
                System.out.println("connection closed");
            }

        }
    }
}
