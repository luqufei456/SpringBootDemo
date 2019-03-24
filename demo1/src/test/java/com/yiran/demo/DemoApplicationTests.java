package com.yiran.demo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DemoApplicationTests {

	// 注入在运行时分配的HTTP端口的字段或方法/构造函数参数级别的注释
	@Value(value = "${local.server.port}")
	private int port;

	private URL base;

	@Autowired
	private TestRestTemplate template;

	@Before
	public void setUp() throws MalformedURLException{
		// TODO 因为我们修改了 content-path 所以请求后面要带上
		this.base = new URL("http://localhost:" + port + "/demo1/demo1");
	}

	@Test
	public void demo1(){
		ResponseEntity<String> response = template.getForEntity(base.toString(), String.class);
		assertEquals(response.getBody(), "hello YiRan");
	}


}
