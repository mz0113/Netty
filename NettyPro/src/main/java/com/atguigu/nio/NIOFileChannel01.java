package com.atguigu.nio;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 本地文件写入
 */
public class NIOFileChannel01 {
    public static void main(String[] args) {
        String str = "hello,尚硅谷";
        //创建一个输出流
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("D:\\file01.txt");
            FileChannel fileChannel = fileOutputStream.getChannel();
            byte[] bytes = new byte[1024*1024*50];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = 'a';
            }
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024*1024*50);
            byteBuffer.put(bytes);

            //切换为读取模式
            byteBuffer.flip();

            fileChannel.write(byteBuffer);
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
