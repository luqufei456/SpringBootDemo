package com.verify.annotation;

import java.lang.annotation.*;

/**
 * 锁的注解
 *
 */
@Target(ElementType.METHOD) // 用来说明该注解可以被声明在那些元素之前
@Retention(RetentionPolicy.RUNTIME) // 生命周期
@Documented // 表明这个注解应该被 javadoc工具记录
@Inherited // 表明这个注解被自动继承，但是对已经实现接口的注解无效
public @interface LocalLock {

    /**
     * @author fly
     */
    String key() default "";

    /**
     * 过期时间 TODO 由于用的 guava 暂时就忽略这属性吧 集成 redis 需要用到，单位为秒
     *
     * @author fly
     */
    int expire() default 5;
}