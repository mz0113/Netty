package com.atguigu.nio;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 透过MappedBuffer，可让文件直接在内存（堆外内存）修改，操作系统不需要拷贝一次
 * 向已存在的文件后追加内容。则应该使用RandomAccessFile。
 */
public class NIOMappedByteBuffer {
    public static void main(String[] args) {
        try {
            //Java提供了一个可以对文件随机访问的操作，访问包括读和写操作。改类名为RandomAccessFile。该类的读写是基于指针的操作。
            RandomAccessFile randomAccessFile = new RandomAccessFile("d:\\01.txt","rw");
            //如果是inputStream或者outputStream得到的channel,不是同时可读可写的，map操作会抛出NonReadableChannelException
            FileChannel channel = randomAccessFile.getChannel();


            // “5”是映射到内存的大小（不是索引位置，即将多少个字节映射到内存）
            MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE,0, 1024*10);
            //1KB = 1024byte,1M = 1024KB,100M
            byte[] bytes = new byte[1024*1];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = 'a';
            }
            mappedByteBuffer.put(bytes,0,bytes.length);
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = 'b';
            }
            mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE,1024*10*5, 1024*10);
            mappedByteBuffer.put(bytes,0,bytes.length);

            randomAccessFile.close();
            System.out.println("success");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
