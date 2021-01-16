package com.atguigu.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 本地文件读取
 */
public class NIOFileChannel02 {
    public static void main(String[] args) {
        //创建一个输出流
        try {
            File file = new File("D:\\file01.txt");
            FileInputStream fileInputStream = new FileInputStream(file);
            FileChannel fileChannel = fileInputStream.getChannel();

            ByteBuffer byteBuffer = ByteBuffer.allocate((int)file.length());

            fileChannel.read(byteBuffer);
            fileInputStream.close();

            System.out.println(new String(byteBuffer.array()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
