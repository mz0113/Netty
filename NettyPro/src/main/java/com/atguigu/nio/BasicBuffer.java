package com.atguigu.nio;

import java.nio.IntBuffer;

public class BasicBuffer {
    public static void main(String[] args) {
        IntBuffer intBuffer = IntBuffer.allocate(5);

        //向buffer中存放数据
        for (int i = 0; i < intBuffer.capacity(); i++) {
            intBuffer.put(i * 2);
        }

        //buffer读写切换
        intBuffer.flip();

        //读取
        while (intBuffer.hasRemaining()) {
            System.out.println(intBuffer.get());
        }
    }
}
