package com.yiran.nio;

import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * 通道之间的数据传输
 * 这两个方法会覆盖原来的数据
 */
public class FileChannelDemo1 {
    public static void main(String[] args) throws Exception {
        RandomAccessFile fromFile = new RandomAccessFile("fromFile.txt", "rw");
        FileChannel fromChannel = fromFile.getChannel();

        RandomAccessFile toFile = new RandomAccessFile("toFile.txt", "rw");
        FileChannel toChannel = toFile.getChannel();

        long position = 0;
        long count = fromChannel.size();

        toChannel.transferFrom(fromChannel, position, count);

        /*RandomAccessFile fromFile = new RandomAccessFile("fromFile.txt", "rw");
        FileChannel fromChannel = fromFile.getChannel();

        RandomAccessFile toFile = new RandomAccessFile("toFile.txt", "rw");
        FileChannel toChannel = toFile.getChannel();

        long position = 0;
        long count = fromChannel.size();

        // 开始的地方，总数量，目标channel，这是个覆盖操作，
        fromChannel.transferTo(position, count, toChannel);*/
    }
}
