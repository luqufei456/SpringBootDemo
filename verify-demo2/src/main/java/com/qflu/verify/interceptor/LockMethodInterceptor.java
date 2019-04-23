package com.qflu.verify.interceptor;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.qflu.verify.annotation.LocalLock;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 本章先基于 本地缓存来做，后续讲解 redis 方案
 */
@Aspect // 开启aop
@Configuration // 通过java代码注册bean
public class LockMethodInterceptor {

    private static final Cache<String, Object> CACHES = CacheBuilder.newBuilder()
            // 最大缓存 100 个
            .maximumSize(100)
            // 设置写缓存后 5 秒钟过期，这里不设置过期时间，因为数据库校验是一个长时间操作
            // .expireAfterWrite(5, TimeUnit.SECONDS)
            .build();

    // 拦截 execution(<修饰符模式>? <返回类型模式> <方法名模式>(<参数模式>) <异常模式>?)
    // 匹配所有目标类的public方法 第一个*代表返回类型，第二个*代表方法名，而..代表任意入参的方法；
    // 该advice作用在有@LocalLock注解的方法下面
    @Around("execution(public * *(..)) && @annotation(com.qflu.verify.annotation.LocalLock)")
    public Object interceptor(ProceedingJoinPoint pjp) {
        // 获取方法签名
        MethodSignature signature = (MethodSignature)pjp.getSignature();
        Method method = signature.getMethod();
        // 方法如果存在这样的注释，则返回指定类型的元素的注释，否则为null。
        LocalLock localLock = method.getAnnotation(LocalLock.class);
        // key 参数都在调用的方法里
        String key = getKey(localLock.key(), pjp.getArgs());
        System.out.println("同步锁key：" + key);
        if (!StringUtils.isEmpty(key)) {
            // 如果caches中存在这个key
            if (CACHES.getIfPresent(key) != null) {
                throw new RuntimeException("请勿重复请求");
            }
            // 否则是第一次请求,就将 key 当前对象压入缓存中
            CACHES.put(key, key);
        }
        try {
            return pjp.proceed();
        } catch (Throwable throwable) {
            throw new RuntimeException("服务器异常");
        } finally {
            // 后端执行完后清除锁，如果演示的话可以将其注释
            CACHES.invalidate(key);
        }
    }

    /**
     * key 的生成策略,如果想灵活可以写成接口与实现类的方式（TODO 后续讲解）
     *
     * @param keyExpress 表达式
     * @param args       参数
     * @return 生成的key
     */
    private String getKey(String keyExpress, Object[] args) {
        // 由于我们定义的 key 为 verify:arg[0] 所以只有遍历到第一个才会被替换，后面的无影响
        for (int i = 0; i < args.length; i++) {
            keyExpress = keyExpress.replace("arg[" + i + "]", args[i].toString());
        }
        return keyExpress;
    }
}