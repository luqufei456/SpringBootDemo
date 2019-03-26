package com.yiran.demo.service.impl;

import com.yiran.demo.entity.User;
import com.yiran.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private static final Map<Long, User> DATABASES = new HashMap<>();

    static {
        DATABASES.put(1L, new User(1L, "yiran", "yiran"));
        DATABASES.put(2L, new User(2L, "chunjue", "chunjue"));
        DATABASES.put(3L, new User(3L, "miku", "miku"));
    }

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    // 在支持Spring Cache的环境下，对于使用@Cacheable标注的方法，
    // Spring在每次执行前都会检查Cache中是否存在相同key的缓存元素，如果存在就不再执行该方法，
    // 而是直接从缓存中获取结果进行返回，否则才会执行并将返回结果存入指定的缓存中。
    // @CachePut也可以声明一个方法支持缓存功能。与@Cacheable不同的是
    // 使用@CachePut标注的方法在执行前不会去检查缓存中是否存在之前执行过的结果，而是每次都会执行该方法，
    // 并将执行结果以键值对的形式存入指定的缓存中。
    @CachePut(value = "user", key = "#user.id")
    @Override
    public User saveOrUpdate(User user) {
        DATABASES.put(user.getId(), user);
        log.info("进入 saveOrUpdate 方法");
        return user;
    }

    // 表示这个方法支持缓存，也可以标记到类上，表示类中所有方法都支持缓存
    // 可以指定三个属性，value、key和condition。
    // value属性指定Cache名称 表示当前方法的返回值是会被缓存在哪个Cache上的，对应Cache的名称。
    // 其可以是一个Cache也可以是多个Cache，当需要指定多个Cache时其是一个数组。
    // condition属性指定发生的条件  https://www.cnblogs.com/fashflying/p/6908028.html
    @Cacheable(value = "user", key = "#id")
    @Override
    public User get(Long id) {
        // TODO 我们就假设它是从数据库读取出来的
        log.info("进入 get 方法");
        return DATABASES.get(id);
    }

    // @CacheEvict是用来标注在需要清除缓存元素的方法或类上的。当标记在一个类上时表示其中所有的方法的执行都会触发缓存的清除操作。
    // @CacheEvict可以指定的属性有value、key、condition、allEntries和beforeInvocation。
    // 其中value、key和condition的语义与@Cacheable对应的属性类似。即value表示清除操作是发生在哪些Cache上的（对应Cache的名称）；
    // key表示需要清除的是哪个key，如未指定则会使用默认策略生成的key；condition表示清除操作发生的条件。
    @CacheEvict(value = "user", key = "#id")
    @Override
    public void delete(Long id) {
        DATABASES.remove(id);
        log.info("进入 delete 方法");
    }
}
