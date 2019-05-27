package com.yiran.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/*
* 这个Demo最关键的就是要理解Buffer双向的概念
* Channel -> Buffer  这里是对Buffer写入
* Buffer -> 变量获取  这里是对Buffer的读出
* 其中的切换是 Buffer.flip(); 方法
* */
public class ChannelDemo1 {
    public static void main(String[] args) throws IOException {
        File file = new File("nio-data.txt");
        // 为了防止文件不存在，先使用file类创建多级目录
        if (file.createNewFile()) {
            // 让文件有内容
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("test , the channel first demo");
            bw.flush();
            bw.close();
        }

        //  RandomAccessFile允许你来回读写文件，也可以替换文件中的某些部分。FileInputStream和FileOutputStream没有这样的功能。
        RandomAccessFile afile = new RandomAccessFile("nio-data.txt", "rw");
        FileChannel inChannel = afile.getChannel();
        // 创建堆缓冲区，以字节为单位
        // 创建堆缓冲区，也可以创建直接(Direct)缓冲区，但是学了JVM之后你就会知道直接缓冲区的缺点，所以用堆缓冲区
        ByteBuffer buf = ByteBuffer.allocate(10);
        // 从该通道inChannel读入给定缓冲区的字节序列，也可以设置从哪里开始读，读多少什么的
        // 简单来说就是把通道中的数据写到缓冲区中
        int bytesRead = inChannel.read(buf);
        while (bytesRead != -1) {
            // 这里
            System.out.println("Read: " + bytesRead);
            // 反转
            // 注意 buf.flip() 的调用，首先读取数据到Buffer，然后反转Buffer,接着再从Buffer中读取数据。
            buf.flip();
            // 如果还能接着读，这里是读出数据给我们看
            while (buf.hasRemaining()) {
                // 返回缓冲区当前位置的字节
                System.out.print((char) buf.get());
            }
            System.out.println();
            buf.clear();
            bytesRead = inChannel.read(buf);
        }
        afile.close();
    }
}
