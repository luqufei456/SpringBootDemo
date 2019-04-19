package com.yiran.demo.utils;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.util.StringUtils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * 需要定义成 Bean
 */
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedisLockHelper {
    // 定界符
    private static final String DELIMITER = "|";

    /**
     * 如果要求比较高可以通过注入的方式分配
     */
    private static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newScheduledThreadPool(10);

    private final StringRedisTemplate stringRedisTemplate;

    public RedisLockHelper(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 该方法只能获取新锁，无法更新旧锁，只能等redis自动删除了才能接着提交表单
     * @param lockKey lockKey
     * @param value   value
     * @param time    超时时间
     * @param unit    过期单位
     * @return true or false
     */
    // 如果没有设置 返回 true 设置了 返回 false
    public boolean tryLock(final String lockKey, final String value, final long time, final TimeUnit unit) {
        return stringRedisTemplate.execute((RedisCallback<Boolean>) connection -> connection.set(lockKey.getBytes(), value.getBytes(), Expiration.from(time, unit), RedisStringCommands.SetOption.SET_IF_ABSENT));
    }

    /**
     * 获取锁，可以更新旧锁
     *
     * @param lockKey lockKey
     * @param uuid    UUID
     * @param timeout 超时时间
     * @param unit    过期单位
     * @return true or false
     *
     * 如果5s内重复提交，则锁的value又会刷新，在finally未被注解的情况下，5s内提交一次则永远无法重复提交。
     * 不过开发环境会执行完方法后立即释放锁
     */
    public boolean lock(String lockKey, final String uuid, long timeout, final TimeUnit unit) {
        // 过期时间也被添加到了value中，设定的是5s
        final long milliseconds = Expiration.from(timeout, unit).getExpirationTimeInMilliseconds();
        //   如果键不存在则新增,存在则不改变已经有的值。这里的
        boolean success = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, (System.currentTimeMillis() + milliseconds) + DELIMITER + uuid);
        // 如果新增成功
        if (success) {
            // 给这个锁添加超时时间
            stringRedisTemplate.expire(lockKey, timeout, TimeUnit.SECONDS);
            System.out.println(System.currentTimeMillis());
        } else {
            // 如果新增失败，获取原来key键对应的值并重新赋新值。
            // getAndSet() 方法会使原来设置的过期时间失效
            String oldVal = stringRedisTemplate.opsForValue().getAndSet(lockKey, (System.currentTimeMillis() + milliseconds) + DELIMITER + uuid);
            // 给这个锁重新设置过期时间
            stringRedisTemplate.expire(lockKey, timeout, TimeUnit.SECONDS);
            // 获取之前设置的时间
            // Pattern.quote(String) 可以将正则表达式转为字面量，相当于转义了，因为String.split()支持正则
            final String[] oldValues = oldVal.split(Pattern.quote(DELIMITER));
            // CacheLock中设置的过期秒数为5s，5s过后就能再次提交了
            if (Long.parseLong(oldValues[0]) <= System.currentTimeMillis()) {
                System.out.println(Long.parseLong(oldValues[0]) + 1);
                System.out.println(System.currentTimeMillis());
                return true;
            }
        }
        return success;
    }

    /**
     * 立即释放锁
     * @see <a href="http://redis.io/commands/set">Redis Documentation: SET</a>
     */
    public void unlock(String lockKey, String value) {
        unlock(lockKey, value, 0, TimeUnit.MILLISECONDS);
    }

    /**
     * 延迟unlock
     *
     * @param lockKey   key
     * @param uuid      client(最好是唯一键的)
     * @param delayTime 延迟时间
     * @param unit      时间单位
     */
    public void unlock(final String lockKey, final String uuid, long delayTime, TimeUnit unit) {
        if (StringUtils.isEmpty(lockKey)) {
            return;
        }
        if (delayTime <= 0) {
            doUnlock(lockKey, uuid);
        } else {
            // 开一个定时器，指定时间后执行
            EXECUTOR_SERVICE.schedule(() -> doUnlock(lockKey, uuid), delayTime, unit);
        }
    }

    /**
     * @param lockKey key
     * @param uuid    client(最好是唯一键的)
     */
    private void doUnlock(final String lockKey, final String uuid) {
        // Pattern.quote(String) 可以将正则表达式转为字面量，相当于转义了，因为String.split()支持正则
        String val = stringRedisTemplate.opsForValue().get(lockKey);
        final String[] values = val.split(Pattern.quote(DELIMITER));
        // 如果获取不到这个锁，说明过期，或者没有这个锁，自然关闭不了，这里直接返回
        if (values.length <= 0) {
            return;
        }
        // 如果uuid对的上，则删掉这个key-val，相当于释放锁
        if (uuid.equals(values[1])) {
            stringRedisTemplate.delete(lockKey);
        }
    }
}
