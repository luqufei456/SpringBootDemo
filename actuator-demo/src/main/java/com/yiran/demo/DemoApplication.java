package com.yiran.demo;

import com.yiran.demo.endpoint.MyEndPoint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnEnabledEndpoint;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Configuration
	static class MyEndpointConfiguration {
		@Bean
		// 判断是否执行初始化代码，即如果用户已经创建了bean，则相关的初始化代码不再执行。
		@ConditionalOnMissingBean
		// 应检查的端点类型。当方法的返回类型@Bean是a Endpoint或a 时推断 EndpointExtension
		@ConditionalOnEnabledEndpoint
		public MyEndPoint myEndPoint() {
			return new MyEndPoint();
		}
	}
}
