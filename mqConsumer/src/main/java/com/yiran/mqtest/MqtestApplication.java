package com.yiran.mqtest;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.List;

@SpringBootApplication
public class MqtestApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(MqtestApplication.class, args);

		// 使用指定的消费者组名称实例化。请命名为唯一的组名
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("consumer-1");
		// 指定名称服务器的地址
		consumer.setNamesrvAddr("10.96.33.184:9876;10.96.33.185:9876");
		// 订阅更多要消费的topic
		consumer.subscribe("TopicTest", "*");
		// 注册回调函数，以便在从代理获取的消息到达时执行。
		consumer.registerMessageListener(new MessageListenerConcurrently() {
			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		});
		// 启动消费者实例
		consumer.start();
		System.out.println("Consumer Started.%n");
		/*consumer.shutdown();*/
	}

}
