package com.yiran.demo.interceptor;

import com.yiran.demo.annotation.CacheLock;
import com.yiran.demo.annotation.CacheParam;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 上一章说过通过接口注入的方式去写不同的生成规则;
 */
@Configuration // 交给Spring管理
public class LockKeyGenerator implements CacheKeyGenerator {

    // 基本类型测试
    public static void main(String[] args) {
        Object obj = 11;
        Field[] fields =  obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            System.out.println(field.getName());
        }
    }

    @Override
    public String getLockKey(ProceedingJoinPoint pjp) {
        // 获取执行方法的签名
        MethodSignature signature = (MethodSignature)pjp.getSignature();
        // 通过签名获得方法
        Method method = signature.getMethod();
        // 通过方法得到方法上的CacheLock注解，没有的话返回为null
        CacheLock lockAnnotation = method.getAnnotation(CacheLock.class);
        // TODO 这里为什么要获取两次参数，因为getArgs()方法返回的Object不能获取到 Annotation
        // 获取连接点所有的参数
        final Object[] args = pjp.getArgs();
        // 获取方法的所有参数
        final Parameter[] parameters = method.getParameters();
        // 定义StringBuilder 非线程安全
        StringBuilder builder = new StringBuilder();
        // TODO 默认解析方法里面带 CacheParam 注解的属性,如果没有尝试着解析实体对象中的
        for (int i = 0; i < parameters.length; i++) {
            final CacheParam annotation = parameters[i].getAnnotation(CacheParam.class);
            if (annotation == null) {
                continue;
            }
            builder.append(lockAnnotation.delimiter()).append(args[i]);
        }
        // 方法里没有带 CacheParam 的属性，开始解析实体对象
        if (StringUtils.isEmpty(builder.toString())) {
            // 为什么是个二维数组 Annotation[参数][参数的注解] https://blog.csdn.net/u011710466/article/details/52888387
            final Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            // 遍历参数
            for (int i = 0; i < parameterAnnotations.length; i++) {
                final Object object = args[i];
                // 拿到对象的字段，不拿父类的
                // TODO 通过上面的测试可以看到，即使是基本数据类型，通过该方法也不会报错，一样能获取很多基本字段
                final Field[] fields = object.getClass().getDeclaredFields();
                // 遍历对象的字段
                for (Field field : fields) {
                    final CacheParam annotation = field.getAnnotation(CacheParam.class);
                    if (annotation == null) {
                        continue;
                    }
                    // 暴力访问
                    field.setAccessible(true);
                    // ReflectionUtils.getField(field, object) 返回这个实体中这个字段的值，因为现在只知道字段名，不知道字段值
                    builder.append(lockAnnotation.delimiter()).append(ReflectionUtils.getField(field, object));
                }
            }
        }
        // 返回key
        return lockAnnotation.prefix() + builder.toString();
    }
}
