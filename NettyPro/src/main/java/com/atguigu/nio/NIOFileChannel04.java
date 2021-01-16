package com.atguigu.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * 使用transferFrom拷贝文件
 */
public class NIOFileChannel04 {
    public static void main(String[] args) {
        try {
            FileInputStream inputStream = new FileInputStream("d:\\a.jpg");
            FileOutputStream outputStream = new FileOutputStream("d:\\b.jpg");
            FileChannel inputStreamChannel = inputStream.getChannel();
            FileChannel outputStreamChannel = outputStream.getChannel();

            //使用transferFrom
            outputStreamChannel.transferFrom(inputStreamChannel,0,inputStreamChannel.size());

            inputStream.close();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
