package com.yiran.mqtest;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class MqtestApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(MqtestApplication.class, args);

		// 声明并初始化一个producer
		// 需要一个producer group名字作为构造方法的参数，这里为 message_stacked
		DefaultMQProducer producer = new DefaultMQProducer("producer");

		// 构造一个大小为2k的字符串 ，char 一个占用2个字节，但是字母 a 只占用一个字节。所以这里相当于2k的字符串
		char[] chs = new char[2048];
		for (int i = 0; i < chs.length; i++) {
			chs[i] = 'a';
		}
		// 将char数组转为消息Stringg

		String strMsg = new String(chs);

		// 设置 namesrv 地址
		producer.setNamesrvAddr("10.96.33.184:9876;10.96.33.185:9876");

		producer.start();

		ExecutorService service = Executors.newFixedThreadPool(20);

		for (int i = 0; i < 20; i++) {
			service.execute(() -> {
				// 百万消息堆积能力测试
				for (int j = 0; j < 1000000; j++) {
					try {
						Message msg = new Message(
								"TopicTest",
								"TagA",
								strMsg.getBytes(RemotingHelper.DEFAULT_CHARSET)
						);
						producer.send(msg);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			});
		}
	}
}
