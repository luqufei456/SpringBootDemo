package com.yiran.demo;

import com.yiran.demo.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@RunWith(SpringRunner.class)
// 指定spring-boot的启动类 classes=DemoApplication.class 为默认值
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
// @SpringApplicationConfiguration(classes = Application.class)// 1.4.0 前版本
public class DemoApplicationTests{

	private static final Logger log = LoggerFactory.getLogger(DemoApplicationTests.class);

	@Autowired
	private TestRestTemplate template;

	// 等价于 @Value(value="${local.server.port}")
	@LocalServerPort
	private int port;

	@Test
	public void test01() {
		// 添加用户
		template.postForEntity("http://localhost:" + port + "/users", new User("user1", "pass1"), Integer.class);
		log.info("[添加用户成功]\n");

		// 查询所有用户
		// TODO 如果是返回的集合,要用 exchange 而不是 getForEntity ，后者需要自己强转类型
		ResponseEntity<List<User>> responseEntity = template.exchange("http://localhost:" + port + "/users", HttpMethod.GET
				, null, new ParameterizedTypeReference<List<User>>() {
		});
		final List<User> body = responseEntity.getBody();
		log.info("[查询所有] - [{}]\n", body);

		// 根据主键id查询用户
		Integer userId = body.get(0).getId();
		ResponseEntity<User> responseEntity2 = template.getForEntity("http://localhost:" + port + "/users/{id}", User.class, userId);
		log.info("[主键查询] - [{}]\n", responseEntity2.getBody());

		// 根据主键修改用户
		template.put("http://localhost:" + port + "/users/{id}", new User("yiran", "yiran"), userId);
		log.info("[修改用户成功]\n");

		// 根据主键删除用户
		template.delete("http://localhost:" + port + "/users/{id}", userId);
		log.info("[删除用户成功]");
	}

}
