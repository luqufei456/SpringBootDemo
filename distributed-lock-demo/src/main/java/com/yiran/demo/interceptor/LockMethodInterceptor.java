package com.yiran.demo.interceptor;

import com.yiran.demo.annotation.CacheLock;
import com.yiran.demo.utils.RedisLockHelper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * redis 方案
 */
@Aspect
@Configuration // 交给 Spring 管理
public class LockMethodInterceptor {

    @Autowired
    public LockMethodInterceptor(RedisLockHelper redisLockHelper, CacheKeyGenerator cacheKeyGenerator) {
        this.redisLockHelper = redisLockHelper;
        this.cacheKeyGenerator = cacheKeyGenerator;
    }

    private final RedisLockHelper redisLockHelper;
    private final CacheKeyGenerator cacheKeyGenerator;

    // 拦截 execution(<修饰符模式>? <返回类型模式> <方法名模式>(<参数模式>) <异常模式>?)
    // 匹配所有目标类的public方法 第一个*代表返回类型，第二个*代表方法名，而..代表任意入参的方法；
    // 该advice作用在有@CacheLock注解的方法下面
    @Around("execution(public * *(..)) && @annotation(com.yiran.demo.annotation.CacheLock)")
    public Object interceptor(ProceedingJoinPoint pjp) {
        // 获取签名、获取方法、获取注解
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        CacheLock lock = method.getAnnotation(CacheLock.class);
        // 如果前缀为null或""
        if (StringUtils.isEmpty(lock.prefix())) {
            throw new RuntimeException("lock key not null...");
        }
        // 根据 pjp 获得 key
        final String lockKey = cacheKeyGenerator.getLockKey(pjp);
        String value = UUID.randomUUID().toString();
        try {
            // 假设上锁成功，但是设置过期时间失效，以后拿到的都是 false
            System.out.println(lockKey);
            final boolean success = redisLockHelper.lock(lockKey, value, lock.expire(), lock.timeUnit());
            if (!success) {
                throw new RuntimeException("重复提交");
            }
            try {
                return pjp.proceed();
            } catch (Throwable throwable) {
                throw new RuntimeException("系统异常");
            }
        } finally {
            // TODO 如果演示的话需要注释该代码;实际应该放开，这是为了不让同样的表单同时被处理，可能造成线程安全问题
            // TODO 同时处理可能出现线程安全问题，但是顺序处理只会更新数据，所以无碍
            // redisLockHelper.unlock(lockKey, value);
        }
    }
}
