package com.atguigu.nio.zerocopy.client;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class ZeroClient {
    static String path = "F:\\studyClass\\jk\\123\\视频\\视频\\VID_20170412_122127.mp4";

    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(true);
        boolean connect = socketChannel.connect(new InetSocketAddress("127.0.0.1", 6666));

        if (connect) {
            FileChannel fileChannel = new FileInputStream(path).getChannel();
            Long size = fileChannel.size();
            int B = 8 * 1024 * 1024;
            Long batch = size / B;
            Long mod = size % B;

            long l = System.currentTimeMillis();
            System.out.println(batch+(mod!=0?1:0) +"次");
            for (Long i = 0L; i < batch+(mod!=0?1:0); i++) {
                Long count = fileChannel.transferTo(i*B,B,socketChannel);
                System.out.println(count+" ->count");
            }
            System.out.println(System.currentTimeMillis()-l);
        }else{
            System.out.println("connect failed");
        }
        System.in.read();
    }
}
