package com.yiran.demo;

import com.yiran.demo.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

    private Logger log = LoggerFactory.getLogger(DemoApplicationTests.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    @Test
    public void get() throws InterruptedException {
        // TODO 测试线程安全
        // 固定线程池，固定1000个线程
        ExecutorService executorService = Executors.newFixedThreadPool(1000);
        IntStream.range(0, 1000).forEach( i ->
                // 这里已经循环完1000次才会出去，所以一定是开了1000个任务
                // increment 以增量的方式存储值，这里的意思就是开1000个线程，增加1000次，看是否线程安全
                // opsForValue 是键值操作器，还有很多其他操作器
                executorService.execute(() -> stringRedisTemplate.opsForValue().increment("test", 1))
        );

        // TODO 这里是为了保证读到自增的结果，不加这一部分，main线程仍然会等待线程池中的子线程执行完毕后才关闭
        // TODO junit中，如果主线程结束了，会直接回调TestResult的wasSuccessful方法，然后调用System.exit()杀死jvm，所以会直接结束
        // 阻止新任务的提交，但是原本已经提交的，不会受到影响，当已提交的任务全部完成后，中断闲置的线程
        // 不shutdown() 线程池，main线程不会结束
        executorService.shutdown();
        while (true) {
            // 调用shutdown()方法后，且已提交的任务完成后，才会返回true
            if (executorService.isTerminated()) {
                System.out.println("线程池执行完毕");
                break;
            }
            Thread.sleep(1000);
        }

        final String count = stringRedisTemplate.opsForValue().get("test");
        log.info("[自增结果] - [{}]", count);

        stringRedisTemplate.opsForValue().set("key1", "val1");
        final String val1 = stringRedisTemplate.opsForValue().get("key1");
        log.info("[字符缓存结果] - [{}]", val1);
        // TODO 以下只演示整合，具体Redis命令可以参考官方文档，Spring Data Redis 只是改了个名字而已，Redis支持的命令它都支持
        String key = "yiran:user:1";
        redisTemplate.opsForValue().set(key, new User(1L, "yiran", "yiran"));
        // TODO 对应 String（字符串）
        final User user = (User) redisTemplate.opsForValue().get(key);
        log.info("[对象缓存结果] - [{}]", user);
    }

    @Test
    public void test01() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        IntStream.range(0, 5).forEach( i ->
                executorService.execute(() -> {
                    for (int x = 0; x < 200; x++) {
                        stringRedisTemplate.opsForValue().increment("test", 1);
                    }
                })
        );
        // 这里是为了保证线程能够执行完，不被junit的System.exit()强行关闭，否则线程池会拒绝任务
        executorService.shutdown();
        while (true) {
            // 调用shutdown()方法后，且已提交的任务完成后，才会返回true
            if (executorService.isTerminated()) {
                System.out.println("线程池执行完毕");
                break;
            }
            Thread.sleep(1000);
        }
    }

    @Test
    public void getTest2() {
        System.out.println(stringRedisTemplate.opsForValue().get("test2"));
    }

    @Test
    public void test02() {
        new Thread(() -> {
                try {
                    TimeUnit.SECONDS.sleep(10000);
                    System.out.println("子线程结束");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }).start();
        System.out.println("结束");
    }

}
