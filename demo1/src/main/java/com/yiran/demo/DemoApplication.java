package com.yiran.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/*
 * 其中 @RestController 等同于 （@Controller 与 @ResponseBody）
 * 将跳转页面改变成直接返回结果
 * */
@SpringBootApplication
@RestController
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@GetMapping("/demo1")
	public String demo1(){
		return "hello YiRan";
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext context){
		// 目的是
		return args -> {
			System.out.println("查看 Spring Boot 默认为我们配置的 Bean: ");
			String [] beanNames = context.getBeanDefinitionNames();
			Arrays.sort(beanNames);
			// 将 beanNames 转成流遍历输出
			Arrays.stream(beanNames).forEach(System.out::println);
		};
	}

}
