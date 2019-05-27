package com.yiran.nio;

import java.io.FileWriter;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class BufferDemo1 {
    public static void main(String[] args) throws Exception {
        RandomAccessFile aFile = new RandomAccessFile("nio-data.txt", "rw");
        FileChannel inChannel = aFile.getChannel();
        RandomAccessFile fFile = new RandomAccessFile("fromFile.txt", "rw");
        FileChannel fChannel = fFile.getChannel();
        // 创建容量为48字节的缓冲区
        ByteBuffer buf = ByteBuffer.allocate(48);

        // 把通道的数据写到缓冲区
        int bytesRead = inChannel.read(buf);
        while (bytesRead != -1) {
            // 切换成读模式
            buf.flip();
            // 一个参数，缓冲区，第二个参数，开始传输文件的位置
            fChannel.write(buf,fChannel.size());
            // 如果还能读就循环
            while (buf.hasRemaining()) {
                // 每次读1字节
                System.out.print((char) buf.get());
            }
            // 准备写缓冲区
            buf.clear();
            bytesRead = inChannel.read(buf);
        }
        aFile.close();
    }
}
