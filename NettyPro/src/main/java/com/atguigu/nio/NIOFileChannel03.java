package com.atguigu.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 从文件1拷贝到文件2
 */
public class NIOFileChannel03 {
    public static void main(String[] args) {
        try {
            FileInputStream fileInputStream = new FileInputStream("D:\\file01.txt");
            FileChannel inChannel = fileInputStream.getChannel();

            FileOutputStream fileOutputStream = new FileOutputStream("D:\\file02.txt");
            FileChannel outChannel = fileOutputStream.getChannel();

            ByteBuffer byteBuffer = ByteBuffer.allocate(5);

            while (true) {
                //必须clear，否则limit=position的话，再read永远是返回0
                //但如果一开始file01就没数据，那read就是-1
                byteBuffer.clear();
                int read = inChannel.read(byteBuffer);
                if (read==-1) {
                    break;
                }
                byteBuffer.flip();
                outChannel.write(byteBuffer);
            }
            fileInputStream.close();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
