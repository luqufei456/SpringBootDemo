package com.yiran.demo;

import com.yiran.demo.entity.User;
import com.yiran.demo.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {
    private static Logger log = LoggerFactory.getLogger(DemoApplicationTests.class);

    @Autowired
    private UserRepository userRepository;

    /*
    * SQLFeatureNotSupportedException: 这个 org.postgresql.jdbc.PgConnection.createClob() 方法尚未被实作。
    * 大意就是JPA要验证pgsql的一个方法，但是jdbc没有实现，但是这个错误呢并不影响，所以下面的方法把这块验证给屏蔽掉。
    * spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.PostgreSQLDialect
    * spring.jpa.properties.hibernate.temp.use_jdbc_metadata_defaults = false
    * */

    @Test
    public void test01() {
        final User user = userRepository.save(new User("yiran", "yiran"));
        log.info("[添加成功] - [{}]", user);
        final List<User> users = userRepository.findAllByUsername("yiran");
        log.info("[条件查询] - [{}]", users);
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("username")));
        final Page<User> userPage = userRepository.findAll(pageable);
        log.info("[分页+排序+查询所有] - [{}]", userPage.getContent());
        // 这里根据主键查询到的结构是一个Optional类型
        userRepository.findById(userPage.getContent().get(0).getId()).ifPresent(user1 -> log.info("[主键查询] - [{}]", user1));
        final User edit = userRepository.save(new User(user.getId(), "yiranUpdate", "yiranUpdate"));
        log.info("[修改成功] - [{}]", edit);
        userRepository.deleteById(edit.getId());
        log.info("[删除主键为 {} 成功] - [{}]", user.getId());
    }
}
