package com.atguigu.nio.zerocopy.client;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class OldClient {
    static String path = "F:\\studyClass\\jk\\123\\视频\\视频\\VID_20170412_122127.mp4";
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(true);
        boolean connect = socketChannel.connect(new InetSocketAddress("127.0.0.1", 6666));

        if (connect) {
            FileChannel fileChannel = new FileInputStream(path).getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(Math.toIntExact(fileChannel.size()));
            long l = System.currentTimeMillis();
            fileChannel.read(byteBuffer);
            byteBuffer.flip();
            socketChannel.write(byteBuffer);
            System.out.println(System.currentTimeMillis()-l);
        }else{
            System.out.println("connect failed");
        }
        System.in.read();
    }
}
